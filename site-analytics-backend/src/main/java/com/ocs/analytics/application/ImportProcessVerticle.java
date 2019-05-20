package com.ocs.analytics.application;

import com.ocs.analytics.domain.FileUpload;
import com.ocs.analytics.domain.SiteStatistic;
import com.ocs.analytics.domain.SiteStatistics;
import com.ocs.analytics.reactivex.domain.SiteStatisticsService;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
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
    private static final String IMPORT_PATTERN = "^[0-9]{10},\\d+?,\\d+?,\\d+?,\\d+?$";
    private io.vertx.reactivex.core.Vertx rxVertx;
    private SiteStatisticsService siteStatisticsService;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        this.rxVertx = new io.vertx.reactivex.core.Vertx(vertx);
        this.siteStatisticsService = SiteStatisticsService.createProxy(rxVertx);
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
                                    .filter(string -> string.trim().matches(IMPORT_PATTERN))
                                    .doFinally(csvFile::close)
                            )
                            .map(string -> SiteStatistic.from(string))
                            .map(siteStatistic -> siteStatistics.addStatistic(siteStatistic))
                            // Clean up when we're finished.
                            .doFinally(() -> this.cleanup(fileUpload.getUploadedFileName()))
                            .doOnComplete(() -> LOGGER.debug("Successfully processed the file, added {} items to the site statistics.", siteStatistics.getStatistics().size()))
                            .doOnComplete(() -> this.enrichStatistics(siteStatistics, message))
                            .doOnError(throwable -> message.fail(1, "Something went wrong " + throwable.getMessage()))
                            .subscribe();

                });

    }

    private void enrichStatistics(SiteStatistics siteStatistics, Message<JsonObject> message) {
        this.siteStatisticsService
                .rxEnrichAnalytics(siteStatistics)
                .subscribe(result -> message.reply(result.toJson()),
                        throwable -> message.fail(2, "Something went wrong while enriching the site statistics: " + throwable.getMessage()));
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
