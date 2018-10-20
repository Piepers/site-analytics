package com.ocs.analytics.application;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.templ.FreeMarkerTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        StaticHandler staticHandler = StaticHandler.create();
        staticHandler.setCachingEnabled(false);
        router.route("/static/*").handler(staticHandler);
        router.route("/").handler(this::indexHandler);

        Router subRouter = Router.router(vertx);
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

    private void indexHandler(RoutingContext routingContext) {
        routingContext.put("title", "Site Analytics");
        templateEngine
                .rxRender(routingContext, "templates", "/index.ftl")
                .subscribe(result -> {
                    routingContext.response().putHeader("Content-Type", "text/html");
                    routingContext.response().end(result);
                }, throwable -> routingContext.fail(throwable));
    }

}
