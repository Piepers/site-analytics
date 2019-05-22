package com.ocs.analytics.application;

import com.ocs.analytics.domain.FileUpload;
import com.ocs.analytics.domain.SiteStatistics;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
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

import java.util.Objects;
import java.util.Optional;

public class HttpServerVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    private final FreeMarkerTemplateEngine templateEngine = FreeMarkerTemplateEngine.create();
    private LocalSessionStore sessionStore;
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
    }

    @Override
    public void start(Future<Void> future) {

        Router router = Router.router(vertx);

        // Enable multipart form data parsing
        router.route().handler(BodyHandler.create().setUploadsDirectory(".vertx/file-uploads"));
        SessionHandler sessionHandler = SessionHandler.create(sessionStore);

        StaticHandler staticHandler = StaticHandler.create();
        router.route().handler(CookieHandler.create());
        router.route().handler(sessionHandler::handle);
        router.route("/static/*").handler(staticHandler);
        router.route("/").handler(this::indexHandler);

        router.post("/import").handler(this::importHandler);

        Router subRouter = Router.router(vertx);
        subRouter.get("/statistics/current").handler(this::getLatestSiteStatistics);
        router.mountSubRouter("/api", subRouter);

        this.vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .rxListen(this.port)
                .subscribe(result -> {
                    LOGGER.debug("The server has been started on port {}", result.actualPort());
                    future.complete();
                }, throwable -> {
                    LOGGER.error("Something went wrong");
                    future.fail(throwable);
                });

        // Passes the weather data to the client.
        this.rxVertx
                .eventBus()
                .<JsonObject>consumer("weather-data-enriched", message -> {

                });
    }

    private void getLatestSiteStatistics(RoutingContext routingContext) {
        routingContext.response().putHeader("Content-Type", "application/json");
        Session session = routingContext.session();
        LOGGER.debug("The session id is now: {}", session.id());
        SiteStatistics ss = routingContext.session().get("sitestatistics");
        if (Objects.isNull(ss)) {
            routingContext.response().end(new JsonObject().put("result", "none").encode());
        } else {
            routingContext.response().end(ss.toJson().encode());
        }
    }

    private void importHandler(RoutingContext routingContext) {
        // Offload the processing of the file to another Verticle so that we can respond immediately.
        Observable
                .fromIterable(routingContext.fileUploads())
                .flatMapSingle(fileUpload -> {
                    LOGGER.debug("Processing file: {}, {}, {}", fileUpload.fileName(), fileUpload.name(), fileUpload.uploadedFileName());
                    // Put the contents of what we upload into a wrapper.
                    FileUpload wrapper = FileUpload.from(fileUpload);
                    // To be able to send this on the event-bus, map it to a JsonObject.
                    JsonObject jsonObject = JsonObject.mapFrom(wrapper);
                    // Send the message and let the handler wait for the response.
                    vertx
                            .eventBus()
                            .<JsonObject>rxSend("file-upload", jsonObject)
                            .doOnSuccess(message -> LOGGER.debug("An import has been processed with {} items.", message.body().getJsonArray("statistics", new JsonArray()).size()))
                            .flatMap(message -> Single.just(new SiteStatistics(message.body())))
                            .doOnSuccess(siteStatistics -> routingContext.session().put("importing", false))
                            .subscribe(siteStatistics -> routingContext.session().put("sitestatistics", siteStatistics),
                                    throwable -> LOGGER.error("Something went wrong while importing the file.", throwable));
                    // Just return with a message that we are not really going to use (could have used a completable too).
                    return Single.just(new JsonObject().put("message", "ok"));
                })
                .toList()
                .doOnSuccess(jsonObjects -> routingContext.session().put("importing", true))
                .flatMap(jsonObjects -> this.renderIndex(routingContext))
                // But return immediately (don't wait for the file(-s) to be processed).
                .subscribe(result -> routingContext.response().putHeader("Content-Type", "text/html").end(result),
                        throwable -> routingContext.fail(throwable));


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
