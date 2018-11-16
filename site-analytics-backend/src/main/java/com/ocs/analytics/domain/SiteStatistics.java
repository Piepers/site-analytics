package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper for {@link SiteStatistic} that represents the file that was imported and the enriched {@link SiteStatistic}
 * records that contain the weather measurements. This object will be returned as a response to importing a site-
 * statistics file.
 *
 * @author Bas Piepers
 */
@DataObject
public class SiteStatistics implements JsonDomainObject {

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

    /**
     * Add an instance of statistics based on the incoming csv.
     *
     * @param csv, the csv record of which we assume that it contains certain columns in certain formats.
     */
    public void addStatisticsFromCsv(String csv) {
        this.statistics.add(SiteStatistic.from(csv));

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
