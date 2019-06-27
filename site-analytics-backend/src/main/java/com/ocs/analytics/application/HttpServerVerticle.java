package com.ocs.analytics.application;

import com.ocs.analytics.domain.FileUpload;
import com.ocs.analytics.domain.SiteStatistics;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.stomp.BridgeOptions;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.stomp.StompServer;
import io.vertx.reactivex.ext.stomp.StompServerHandler;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.Session;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.CookieHandler;
import io.vertx.reactivex.ext.web.handler.SessionHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.sstore.LocalSessionStore;
import io.vertx.reactivex.ext.web.templ.FreeMarkerTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class HttpServerVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);
    private static final Integer ONE_MINUTE = 1000 * 60;
    private static final Integer THREE_MINUTES = 1000 * 180;
    private static final String UPDATE_STOMP_DESTINATION = "weather-data-enriched";
    private final FreeMarkerTemplateEngine templateEngine = FreeMarkerTemplateEngine.create();
    private LocalSessionStore sessionStore;
    private Map<String, SiteStatisticsDto> localStatisticsStore;
    private int port;
    private io.vertx.reactivex.core.Vertx rxVertx;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);

        this.port = Optional
                .ofNullable(context
                        .config()
                        .getJsonObject("http_server"))
                .map(o -> o.getInteger("port")).orElse(5000);
        this.rxVertx = new io.vertx.reactivex.core.Vertx(vertx);
        sessionStore = LocalSessionStore.create(rxVertx);
        localStatisticsStore = new HashMap<>();
    }

    @Override
    public void start(Future<Void> future) {
        StompServerOptions stompServerOptions = new StompServerOptions()
                .setPort(-1)
                .setWebsocketBridge(true)
                .setWebsocketPath("/stomp");

        BridgeOptions bridgeOptions = new BridgeOptions()
                .addOutboundPermitted(new PermittedOptions().setAddress(UPDATE_STOMP_DESTINATION));

        StompServer stompServer = StompServer
                .create(vertx, stompServerOptions)
                .handler(StompServerHandler.create(vertx).bridge(bridgeOptions));

        Router router = Router.router(vertx);

        // Enable multipart form data parsing
        router.route().handler(BodyHandler.create().setUploadsDirectory(".vertx/file-uploads"));
        SessionHandler sessionHandler = SessionHandler.create(sessionStore);

        StaticHandler staticHandler = StaticHandler.create().setCachingEnabled(false);

        router.route().handler(CookieHandler.create());
        router.route().handler(sessionHandler::handle);

        router.route("/").handler(this::indexHandler);
        router.route().handler(staticHandler);

        router.post("/import").handler(this::importHandler);

        Router subRouter = Router.router(vertx);
        subRouter.get("/statistics/current").handler(this::getLatestSiteStatistics);
        subRouter.get("/statistics/first").handler(this::first);
        subRouter.get("/statistics/next").handler(this::next);
        subRouter.get("/statistics/previous").handler(this::previous);
        subRouter.get("/statistics/last").handler(this::last);

        router.mountSubRouter("/api", subRouter);

        this.vertx
                .createHttpServer(new HttpServerOptions()
                        .setCompressionSupported(true)
                        .setWebsocketSubProtocols("v10.stomp, v11.stomp, v12.stomp"))
                .websocketHandler(stompServer.webSocketHandler())
                .requestHandler(router::accept)
                .rxListen(this.port)
                .subscribe(result -> {
                    LOGGER.debug("The server has been started on port {}", result.actualPort());
                    future.complete();
                }, throwable -> {
                    LOGGER.error("Something went wrong");
                    future.fail(throwable);
                });

        // Poll for status of the local statistics just for debugging purposes.
        rxVertx
                .setPeriodic(ONE_MINUTE, handler -> LOGGER.debug("Local statistics store contains: {} items.", Objects.nonNull(localStatisticsStore) ? localStatisticsStore.size() : 0));

        // Clean up the local statistics store for sessions that no longer exist.
        rxVertx
                .setPeriodic(ONE_MINUTE, handler -> {
                    LOGGER.debug("Local statistics store contains: {} items.", Objects.nonNull(localStatisticsStore) ? localStatisticsStore.size() : 0);
                    if (Objects.nonNull(localStatisticsStore)) {
                        Set<String> toBeDeleted = new HashSet<>(localStatisticsStore.keySet().stream().collect(Collectors.toList()));

                        Observable
                                .fromIterable(localStatisticsStore.keySet())
                                .flatMapMaybe(key -> sessionStore.rxGet(key))
                                .doOnNext(session -> LOGGER.debug("Session key {} still exists. Don't delete.", session.id()))
                                .doFinally(() -> LOGGER.debug("To be deleted contains {} items which will now be deleted from the statistics store. ", toBeDeleted.size()))
                                .doFinally(() -> toBeDeleted.stream().forEach(key -> localStatisticsStore.remove(key)))
                                .subscribe(session -> {
                                            LOGGER.debug("In subscribe onNext (?) for session: {}", session.id());
                                            toBeDeleted.remove(session.id());
                                        },
                                        throwable -> LOGGER.debug("Something went wrong while asserting which sessions no longer exist"),
                                        () -> {
                                            LOGGER.debug("Completed. No sessions found");
                                        });

//                localStatisticsStore
//                        .keySet()
//                        .stream()
//                        .peek(key -> LOGGER.debug("Checking session key: {}", key))
//                        .forEach(key -> sessionStore
//                                .rxGet(key)
//                                .doOnComplete(() -> LOGGER.debug("Session {} no longer exists. Removing corresponding statistics.", key))
//                                .subscribe(session -> LOGGER.debug("Session {} still active, don't remove corresponding statistics.", session.id()),
//                                        throwable -> LOGGER.error("Something went wrong while checking for existence of session key .", throwable),
//                                         Session did not exist.
//                                        () -> this.localStatisticsStore.remove(key)));
                    }
                });
    }

    private void previous(RoutingContext routingContext) {
        SiteStatisticsDto statisticsDto = this.getPrepareRoutingContext(routingContext);
        if (Objects.isNull(statisticsDto)) {
            routingContext
                    .response()
                    .end(new JsonObject().encode());
        } else {
            statisticsDto.previous();
            routingContext
                    .response()
                    .end(statisticsDto.getPageAsJson().encode());
        }
    }

    private void last(RoutingContext routingContext) {
        SiteStatisticsDto statisticsDto = this.getPrepareRoutingContext(routingContext);

        if (Objects.isNull(statisticsDto)) {
            routingContext
                    .response()
                    .end(new JsonObject().encode());
        } else {
            statisticsDto.last();
            routingContext
                    .response()
                    .end(statisticsDto.getPageAsJson().encode());
        }
    }

    private void next(RoutingContext routingContext) {
        SiteStatisticsDto statisticsDto = this.getPrepareRoutingContext(routingContext);
        if (Objects.isNull(statisticsDto)) {
            routingContext
                    .response()
                    .end(new JsonObject().encode());
        } else {
            statisticsDto.next();
            routingContext
                    .response()
                    .end(statisticsDto.getPageAsJson().encode());
        }
    }

    private void first(RoutingContext routingContext) {
        SiteStatisticsDto statisticsDto = this.getPrepareRoutingContext(routingContext);
        if (Objects.isNull(statisticsDto)) {
            routingContext
                    .response()
                    .end(new JsonObject().encode());
        } else {
            statisticsDto.first();
            routingContext
                    .response()
                    .end(statisticsDto.getPageAsJson().encode());
        }
    }

    private SiteStatisticsDto getPrepareRoutingContext(RoutingContext context) {
        String sessionId = context.session().id();
        SiteStatisticsDto statisticsDto = this.localStatisticsStore.get(sessionId);
        context
                .response()
                .putHeader("Content-Type", "application/json");
        return statisticsDto;
    }

    private void getLatestSiteStatistics(RoutingContext routingContext) {
        routingContext
                .response()
                .putHeader("Content-Type", "application/json");

        Session session = routingContext.session();
        SiteStatisticsDto dto = this.localStatisticsStore.get(session.id());

        if (Objects.isNull(dto)) {
            routingContext
                    .response()
                    .end(new JsonObject()
                            .put("result", "none")
                            .encode());
        } else {
            routingContext
                    .response()
                    .end(dto
                            .getPageAsJson()
                            .encode());
        }
    }

    private void importHandler(RoutingContext routingContext) {
        // We expect only one file at a time
        if (routingContext.fileUploads().size() > 1) {
            routingContext.response().setStatusCode(500).end("We should not receive more than one file at a time.");
        }

        Optional<io.vertx.reactivex.ext.web.FileUpload> fileUpload = routingContext.fileUploads().stream().findFirst();

        // Expect at least something
        if (!fileUpload.isPresent()) {
            routingContext.response().setStatusCode(500).end("No file-upload was found.");
        } else {

            // Offload the processing of the file to another Verticle so that we can respond immediately.
            FileUpload wrapper = FileUpload.from(fileUpload.get());
            LOGGER.debug("Processing file: {} with uploaded filename of: {} and size: {}", wrapper.getFileName(), wrapper.getUploadedFileName(), wrapper.getSize());
            // Process the file and subsribe to the response.
            vertx
                    .eventBus()
                    .<JsonObject>rxSend("file-upload", JsonObject.mapFrom(wrapper))
                    .doOnSuccess(message -> LOGGER.debug("An import has been processed with {} items.", message.body().getJsonArray("statistics", new JsonArray()).size()))
                    .map(message -> new SiteStatistics(message.body()))
                    .map(siteStatistics -> SiteStatisticsDto.fromOrderedStatistics((TreeSet) siteStatistics.getStatistics()))
                    .doOnSuccess(dto -> ((SiteStatisticsDto) dto).first())
                    .doOnSuccess(dto -> this.localStatisticsStore.put(routingContext.session().id(), (SiteStatisticsDto) dto))
                    .map(dto -> ((SiteStatisticsDto) dto).getPageAsJson())
                    .subscribe(jo -> routingContext
                                    .response()
                                    .putHeader("Content-Type", "application/json")
                                    .end(((JsonObject) jo).encode()),
                            throwable -> routingContext
                                    .fail((Throwable) throwable));
        }

//        Observable
//                .fromIterable(routingContext.fileUploads())
//                .map(fileUpload -> FileUpload.from(fileUpload))
//                .doOnNext(wrapper -> LOGGER.debug("Processing file: {}, {}, {}", wrapper.getFileName(), wrapper.getUploadedFileName(), wrapper.getSize()))
//                .map(wrapper -> JsonObject.mapFrom(wrapper))
//                .flatMapSingle(jo -> vertx.eventBus().<JsonObject>rxSend("file-upload", jo))
//                .doOnNext(message -> LOGGER.debug("An import has been processed with {} items.", message.body().getJsonArray("statistics", new JsonArray()).size()))
//                .flatMapSingle(message -> Single.just(new SiteStatistics(message.body())))
//                .map(siteStatistics -> SiteStatisticsDto.fromOrderedStatistics((TreeSet) siteStatistics.getStatistics()))
//                .doOnNext(dto -> ((SiteStatisticsDto) dto).first())
//                .doOnNext(dto -> routingContext.session().put("importing", false))
//                .doOnNext(dto -> this.localStatisticsStore.put(routingContext.session().id(), dto))
//                .flatMapSingle(fileUpload -> {
//                    LOGGER.debug("Processing file: {}, {}, {}", fileUpload.fileName(), fileUpload.name(), fileUpload.uploadedFileName());
//                    // Put the contents of what we upload into a wrapper.
//                    FileUpload wrapper = FileUpload.from(fileUpload);
//                    // To be able to send this on the event-bus, map it to a JsonObject.
//                    JsonObject jsonObject = JsonObject.mapFrom(wrapper);
//                    // Send the message and let the handler wait for the reply.
//                    vertx
//                            .eventBus()
//                            .<JsonObject>rxSend("file-upload", jsonObject)
//                            .doOnSuccess(message -> LOGGER.debug("An import has been processed with {} items.", message.body().getJsonArray("statistics", new JsonArray()).size()))
//                            .flatMap(message -> Single.just(new SiteStatistics(message.body())))
//                            .map(siteStatistics -> SiteStatisticsDto.fromOrderedStatistics((TreeSet) siteStatistics.getStatistics()))
//                            .doOnSuccess(dto -> ((SiteStatisticsDto) dto).first())
//                            .doOnSuccess(dto -> routingContext.session().put("importing", false))
//                            .doOnSuccess(dto -> this.localStatisticsStore.put(routingContext.session().id(), (SiteStatisticsDto) dto))
//                            .subscribe(dto -> vertx
//                                            .eventBus()
//                                            .publish(UPDATE_STOMP_DESTINATION, ((SiteStatisticsDto) dto).getPageAsJson().encode()),
//                                    throwable -> LOGGER.error("Something went wrong while importing the file.", throwable));
//                    // Just return with a message that we are not really going to use (could have used a completable too).
//                    return Single.just(new JsonObject().put("message", "ok"));
//                })
//                .toList()
//                .doOnSuccess(jsonObjects -> routingContext.session().put("importing", true))
//                .flatMap(jsonObjects -> this.renderIndex(routingContext))
//                // But return immediately (don't wait for the file(-s) to be processed).
//                .subscribe(result -> routingContext.response().putHeader("Content-Type", "text/html").end(result),
//                        throwable -> routingContext.fail(throwable));


    }

    private void indexHandler(RoutingContext routingContext) {
        this.renderIndex(routingContext)
                .subscribe(result -> {
                    routingContext.response().putHeader("Content-Type", "text/html");
                    routingContext.response().end(result);
                }, throwable -> routingContext.fail(throwable));
    }

    private Single<Buffer> renderIndex(RoutingContext routingContext) {
        routingContext.put("title", "Site Analytics");
        return templateEngine
                .rxRender(routingContext, "templates", "/index.ftl");
    }

}
