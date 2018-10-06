package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Wrapper for {@link SiteStatistic} that represents the file that was imported.
 *
 * @author Bas Piepers
 */
@DataObject
public class SiteStatistics {
    private final List<SiteStatistic> statistics;

    public SiteStatistics(List<SiteStatistic> statistics) {
        this.statistics = statistics;
    }

    public SiteStatistics() {
        this.statistics = new ArrayList<>();
    }

    public SiteStatistics(JsonObject jsonObject) {
        this.statistics = jsonObject
                .getJsonArray("statistics")
                .stream()
                .map(o -> new SiteStatistic((JsonObject) o))
                .collect(Collectors.toList());
    }

    public List<SiteStatistic> getStatistics() {
        return statistics;
    }

    public SiteStatistics addStatistic(SiteStatistic siteStatistic) {
        this.statistics.add(siteStatistic);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteStatistics that = (SiteStatistics) o;

        return statistics.equals(that.statistics);
    }

    @Override
    public int hashCode() {
        return statistics.hashCode();
    }

    @Override
    public String toString() {
        return "SiteStatistics{" +
                "statistics=" + statistics +
                '}';
    }
}
