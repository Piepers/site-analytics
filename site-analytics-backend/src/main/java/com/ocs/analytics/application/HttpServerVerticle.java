package com.ocs.analytics.application;

import com.ocs.analytics.domain.FileUpload;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.templ.FreeMarkerTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class HttpServerVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    private final FreeMarkerTemplateEngine templateEngine = FreeMarkerTemplateEngine.create();

    private int port;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);

        this.port = Optional
                .ofNullable(context
                        .config()
                        .getJsonObject("http_server"))
                .map(o -> o.getInteger("port")).orElse(5000);
    }

    @Override
    public void start(Future<Void> future) {
//        Router router = Router.router(vertx);
//        router.route().handler(BodyHandler.create().setUploadsDirectory(".vertx/file-uploads"));
//
//        router.post("/import").handler(this::importHandler);
//
//        StaticHandler staticHandler = StaticHandler.create();
//        // TODO: remove in production
//        staticHandler.setCachingEnabled(false);
//        router.route("/static/*").handler(staticHandler);
//        router.route("/").handler(this::indexHandler);
//
        Router router = Router.router(vertx);

        // Enable multipart form data parsing
        router.route().handler(BodyHandler.create().setUploadsDirectory(".vertx/file-uploads"));

        StaticHandler staticHandler = StaticHandler.create();
//         TODO: remove in production
        staticHandler.setCachingEnabled(false);
        router.route("/static/*").handler(staticHandler);
        router.route("/").handler(this::indexHandler);

        router.post("/import").handler(this::importHandler);

        Router subRouter = Router.router(vertx);
        subRouter.route(HttpMethod.GET, "/test-trigger").handler(this::triggerPostRequest);
        router.mountSubRouter("/api", subRouter);

        this.vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(this.port)
                .subscribe(result -> {
                    LOGGER.debug("The server has been started on port {}", result.actualPort());
                    future.complete();
                }, throwable -> {
                    LOGGER.error("Something went wrong");
                    future.fail(throwable);
                });
    }

    // FIXME: remove - is just to test whether we can send and receive something to the KNMI web service that holds historical data.
    private void triggerPostRequest(RoutingContext routingContext) {
        vertx.eventBus().publish("trigger-test", new JsonObject());
        routingContext
                .response()
                .setStatusCode(200)
                .putHeader("Content-Type", "application/json; charset=UTF-8")
                .end(new JsonObject().put("message", "Ok")
                        .encode(), StandardCharsets.UTF_8.name());
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
                            .subscribe(message -> LOGGER.debug("Imported response: {}",
                                    // TODO: implement the response of the processing.
                                    message
                                            .body()
                                            .encode()),
                                    throwable -> LOGGER.error("Something went wrong while importing the file.", throwable));
                    // Just return with a message that we are not really going to use (could have used a completable too).
                    return Single.just(new JsonObject().put("message", "ok"));
                })
                // FIXME: do this differently.
                .toList()
                .flatMap(jsonObjects -> this.renderIndex(routingContext.put("importing", true)))
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
