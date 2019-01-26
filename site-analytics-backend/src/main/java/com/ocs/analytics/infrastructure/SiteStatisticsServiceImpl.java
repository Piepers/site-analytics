package com.ocs.analytics.infrastructure;

import com.ocs.analytics.domain.*;
import io.reactivex.Observable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.parsetools.RecordParser;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.serviceproxy.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link SiteStatisticsService} that instantiates a webclient with a webclient pool (because
 * there is only one instance of this service in the application). This webclient is used to invoke requests to a
 * site that contains historical weather data.
 *
 * @author Bas Piepers
 */
public class SiteStatisticsServiceImpl implements SiteStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteStatisticsServiceImpl.class);
    private static final String WEATHER_BASE_URL = "projects.knmi.nl";
    private static final String WEATHER_REQUEST_PER_HOUR_URL = "/klimatologie/uurgegevens/getdata_uur.cgi";

    private WebClient webClient;
    private Vertx rxVertx;

    public SiteStatisticsServiceImpl(io.vertx.core.Vertx vertx) {
        this.rxVertx = new Vertx(vertx);
        this.webClient = WebClient.create(rxVertx,
                new WebClientOptions().
                        setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                        .setMaxPoolSize(10)
                        .setLogActivity(false));
    }

    @Override
    public void enrichAnalytics(SiteStatistics statistics, Handler<AsyncResult<SiteStatistics>> result) {
        LOGGER.debug("Retrieving data to enrich the site analytics with weather data.");
        SiteStatistic first = statistics.first();
        SiteStatistic last = statistics.last();

        LOGGER.debug("Received first statistic of {}.\nLast statistic of {}.", first, last);

        if (statistics.spansMoreThanAYear()) {
            throw new ServiceException(500, "This service does not allow a longer time range than 12 months for the site-statistics.");
        }

        HistoricalParameters parameters = HistoricalParameters.forWeatherMeasurement(first.year(), first.month(),
                first.day(), last.year(), last.month(), last.day(), first.hour(), last.hour());

        // Create the map to be sent to the website.
        MultiMap form = parameters.asMultiMapForForm();

        webClient
                .post(443, WEATHER_BASE_URL, WEATHER_REQUEST_PER_HOUR_URL)
                .ssl(true)
                .putHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .putHeader("Accept-Encoding", "gzip, deflate, br")
                .putHeader("Content-Type", "application/x-www-form-urlencoded")
                .rxSendForm(form)
                .doOnError(throwable -> LOGGER.error("Something went wrong while sending a form.", throwable))
                .flatMapObservable(response -> this.processResponse(response))
                .doOnComplete(() -> LOGGER.debug("Completed processing the response"))
                .subscribe(string -> this.processOneRecord(statistics, string),
                        throwable -> result.handle(Future.failedFuture(throwable)),
                        () -> {
                            result.handle(Future.succeededFuture(statistics));
                        });
    }

    private Observable<String> processResponse(HttpResponse<Buffer> response) {
        if (response.statusCode() == 200) {
            LOGGER.debug("Response received {}", response.statusMessage());
            return RecordParser
                    .newDelimited("\n", Observable.just(response.body()))
                    .toObservable()
                    .map(buffer -> buffer.toString())
                    .map(string -> string.replaceAll("\\s", ""))
                    .filter(string -> string.matches(WeatherMeasurement.RECORD_PATTERN));
        } else {
            return Observable.error(() -> new ServiceException(response.statusCode(), response.statusMessage()));
        }

    }

    private SiteStatistics processOneRecord(SiteStatistics siteStatistics, String record) {
        return siteStatistics.addMeasurementBasedOnRecord(record);
    }
}
