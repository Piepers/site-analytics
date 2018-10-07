package com.ocs.analytics;

import com.ocs.analytics.application.HttpServerVerticle;
import io.reactivex.Completable;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyticsApplication extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsApplication.class);

    @Override
    public void start(Future<Void> startFuture) {
        // Set the main configuration of our application to be used.
        final ConfigStoreOptions mainConfigStore = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", "config/app-conf.json"));

        // The non-public config store for items like api-keys and other stuff that we do not commit to a public scm
        final ConfigStoreOptions nonPublicConfigStore = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", "config/non-public-conf.json"));

        final ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(nonPublicConfigStore)
                .addStore(mainConfigStore);

        final ConfigRetriever configRetriever = ConfigRetriever.create(this.vertx, options);

        configRetriever
                .rxGetConfig()
                .flatMapCompletable(configuration ->
                        Completable
                                .fromAction(() -> LOGGER.debug("Deploying Analytics Application backend."))
                                .andThen(this.vertx
                                        .rxDeployVerticle(HttpServerVerticle.class.getName(), new DeploymentOptions().setConfig(configuration)))
                                .toCompletable())
                .subscribe(() -> {
                    LOGGER.debug("Application deployed successfully.");
                    startFuture.complete();
                }, throwable -> {
                    LOGGER.debug("Application has not been deployed successfully due to:", throwable);
                    startFuture.fail(throwable);
                });
    }
}

