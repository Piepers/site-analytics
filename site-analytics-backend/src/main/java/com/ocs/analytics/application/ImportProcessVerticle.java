package com.ocs.analytics.application;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes the import of the file by using a service to do so.
 *
 * @author Bas Piepers
 */
public class ImportProcessVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportProcessVerticle.class);
    private WebClient webClient;
    private io.vertx.reactivex.core.Vertx rxVertx;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        this.rxVertx = new io.vertx.reactivex.core.Vertx(vertx);

        this.webClient = WebClient.create(rxVertx,
                new WebClientOptions().
                        setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                        .setMaxPoolSize(10)
                        .setLogActivity(false));

        // TODO: instantiate proxy to service.

    }

    @Override
    public void start() {
        this.vertx.eventBus().<JsonObject>consumer("file-upload", message ->
                LOGGER.debug("Received import message with jsonObject: {}",
                        message.body().encodePrettily()));

        this.vertx.eventBus().<JsonObject>consumer("trigger-test", message -> {
            MultiMap form = MultiMap.caseInsensitiveMultiMap();

            form.add("lang", "nl")
                    .add("byear", "2018")
                    .add("bmonth", "1")
                    .add("bday", "1")
                    .add("eyear", "2018")
                    .add("emonth", "10")
                    .add("eday", "27")
                    .add("bhour", "1")
                    .add("ehour", "24")
                    .add("variabele", "T10N")
                    .add("variabele", "DR")
                    .add("variabele", "RH")
                    .add("variabele", "U")
                    .add("variabele", "R")
                    .add("variabele", "S")
                    .add("stations", "260")
                    .add("submit", "Download dataset");

            webClient
                    .post(443, "projects.knmi.nl", "/klimatologie/uurgegevens/getdata_uur.cgi")
                    .ssl(true)
                    .putHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .putHeader("Accept-Encoding", "gzip, deflate, br")
                    .putHeader("Content-Type", "application/x-www-form-urlencoded")
                    .rxSendForm(form)
                    .subscribe(response -> {
                        LOGGER.debug("Received response: {}", response.statusMessage());
                        LOGGER.debug("Received body of response:\n{}", response.body().toString());
                    }, throwable -> LOGGER.error("Something went wrong while sending a form.", throwable));
        });

    }

}
