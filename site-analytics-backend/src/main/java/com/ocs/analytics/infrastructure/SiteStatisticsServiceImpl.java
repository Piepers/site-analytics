package com.ocs.analytics.infrastructure;

import com.ocs.analytics.domain.SiteStatistics;
import com.ocs.analytics.domain.SiteStatisticsService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;

/**
 * Implementtion of the {@link SiteStatisticsService} that instantiates a webclient with a webclient pool (because
 * there is only one instance of this service in the application). This webclient is used to invoke requests to a
 * site that contains historical weather data.
 *
 * @author Bas Piepers
 *
 */
public class SiteStatisticsServiceImpl implements SiteStatisticsService {

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
        // Ask the statistics to validate itself (must nog contain data from more than a year.)
        // Construct the request with the form data
        // match the response to the records in the statistics instance.
        // TODO: implement
    }
}
