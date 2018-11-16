package com.ocs.analytics.domain;

import com.ocs.analytics.infrastructure.SiteStatisticsServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

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
        return new SiteStatisticsServiceImpl();
    }

    void enrichAnalytics(SiteStatistics statistics, Handler<AsyncResult<SiteStatistics>> result);

}
