package com.ocs.analytics.application;

import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    @Override
    public void start(Future<Void> future) {
        LOGGER.debug("Started HttpServerVerticle...");
        future.complete();
    }

}
