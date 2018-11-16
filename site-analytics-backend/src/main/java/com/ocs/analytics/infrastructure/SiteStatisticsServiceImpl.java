package com.ocs.analytics.infrastructure;

import com.ocs.analytics.domain.SiteStatistics;
import com.ocs.analytics.domain.SiteStatisticsService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public class SiteStatisticsServiceImpl implements SiteStatisticsService {

    @Override
    public void enrichAnalytics(SiteStatistics statistics, Handler<AsyncResult<SiteStatistics>> result) {

    }
}
