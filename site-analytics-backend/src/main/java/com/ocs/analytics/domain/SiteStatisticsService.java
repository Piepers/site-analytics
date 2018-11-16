package com.ocs.analytics.domain;

import com.ocs.analytics.infrastructure.SiteStatisticsServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ServiceException;

/**
 * Is responsible for processing the imported file and enriching its data with history data from a web-site that has
 * historical weather data.
 *
 * @author Bas Piepers
 */
@VertxGen
@ProxyGen
public interface SiteStatisticsService {

    String EVENT_BUS_ADDRESS = "com.ocs.analytics.domain.SiteStatisticsService";

    static SiteStatisticsService create(Vertx vertx) {
        return new SiteStatisticsServiceImpl(vertx);
    }

    /**
     * Based on a fixed url, this method will enrich the already existing analytics records with weather data from a
     * weather station in The Netherlands so that a user can compare weather data with the site visits.
     *
     * @param statistics, the statistics that are expected to contain {@link SiteStatistic} records.
     * @param result,     the result of the enrichment. Might throw a {@link io.vertx.serviceproxy.ServiceException} in case
     *                    enrichment fails.
     * @throws ServiceException in case enrichment fails.
     */
    void enrichAnalytics(SiteStatistics statistics, Handler<AsyncResult<SiteStatistics>> result) throws ServiceException;

}
