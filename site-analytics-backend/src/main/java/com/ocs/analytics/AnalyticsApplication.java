package com.ocs.analytics;

import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyticsApplication extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsApplication.class);

    @Override
    public void start(Future<Void> startFuture) {
        // Set the main configuration of our application to be used.

    }
}
