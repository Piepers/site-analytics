package com.ocs.analytics.application;

import com.ocs.analytics.domain.FileUpload;
import com.ocs.analytics.domain.SiteStatistic;
import com.ocs.analytics.domain.SiteStatistics;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes the import of the file by using a service to do so.
 *
 * @author Bas Piepers
 */
public class ImportProcessVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportProcessVerticle.class);
    //    private WebClient webClient;
    private io.vertx.reactivex.core.Vertx rxVertx;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        this.rxVertx = new io.vertx.reactivex.core.Vertx(vertx);

//        this.webClient = WebClient.create(rxVertx,
//                new WebClientOptions().
//                        setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
//                        .setMaxPoolSize(10)
//                        .setLogActivity(false));

        // TODO: instantiate proxy to service.

    }

    @Override
    public void start() {
        this.vertx
                .eventBus()
                .<JsonObject>consumer("file-upload", message -> {
                    SiteStatistics siteStatistics = new SiteStatistics();
                    LOGGER.debug("Received import message with jsonObject: {}",
                            message.body().encodePrettily());
                    // Map it back to a FileUpload instance.
                    FileUpload fileUpload = new FileUpload(message.body());
                    LOGGER.debug("Fileupload: {}", fileUpload.toString());

                    vertx
                            .fileSystem()
                            .rxOpen(fileUpload.getUploadedFileName(), new OpenOptions())
                            .flatMapObservable(csvFile -> RecordParser
                                            .newDelimited("\n", csvFile)
                                            .toObservable()
                                            .map(buffer -> buffer.toString())
                                            // Expect a delimited string with 5 columns and specific values
                                            .filter(string -> string.matches("^[0-9]{10},\\d+?,\\d+?,\\d+?,\\d+?$"))
                                            .doFinally(csvFile::close)
                            )
                            .map(string -> SiteStatistic.from(string))
                            .map(siteStatistic -> siteStatistics.addStatistic(siteStatistic))
                            // Clean up when we're finished.
                            .doFinally(() -> this.cleanup(fileUpload.getUploadedFileName()))
                            .doOnComplete(() -> LOGGER.debug("Successfully processed the file, added {} items to the site statistics.", siteStatistics.getStatistics().size()))
                            .doOnError(throwable -> LOGGER.error("Something failed while processing the imported file.", throwable))
                            // TODO: call the service that enriches these site statistics.
                            .subscribe(result -> LOGGER.debug("Processed site statistics"),
                                    throwable -> message.fail(1, "Something went wrong " +
                                            throwable.getMessage()),
                                    () -> message.reply(new JsonObject().put("message", "ok")));


                });

//        this.vertx.eventBus().<JsonObject>consumer("trigger-test", message -> {
//            MultiMap form = MultiMap.caseInsensitiveMultiMap();
//
//            form.add("lang", "nl")
//                    .add("byear", "2018")
//                    .add("bmonth", "1")
//                    .add("bday", "1")
//                    .add("eyear", "2018")
//                    .add("emonth", "10")
//                    .add("eday", "27")
//                    .add("bhour", "1")
//                    .add("ehour", "24")
//                    .add("variabele", "T10N")
//                    .add("variabele", "DR")
//                    .add("variabele", "RH")
//                    .add("variabele", "U")
//                    .add("variabele", "R")
//                    .add("variabele", "S")
//                    .add("stations", "260")
//                    .add("submit", "Download dataset");
//
//            webClient
//                    .post(443, "projects.knmi.nl", "/klimatologie/uurgegevens/getdata_uur.cgi")
//                    .ssl(true)
//                    .putHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
//                    .putHeader("Accept-Encoding", "gzip, deflate, br")
//                    .putHeader("Content-Type", "application/x-www-form-urlencoded")
//                    .rxSendForm(form)
//                    .subscribe(response -> {
//                        LOGGER.debug("Received response: {}", response.statusMessage());
//                        LOGGER.debug("Received body of response:\n{}", response.body().toString());
//                    }, throwable -> LOGGER.error("Something went wrong while sending a form.", throwable));
//        });

    }

    private void cleanup(String fileName) {
        LOGGER.debug("Cleaning up file {}", fileName);
        vertx
                .fileSystem()
                .rxDelete(fileName)
                .subscribe(() -> LOGGER.debug("Successfully cleaned up uploaded file."),
                        throwable -> LOGGER.error("Unable to clean up fileupload", throwable));
    }

}
