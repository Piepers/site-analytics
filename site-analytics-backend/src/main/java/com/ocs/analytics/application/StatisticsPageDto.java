package com.ocs.analytics.application;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * One page of site statistics enriched with weather data with not more than one week of session data. This Dto
 * represents what we show in the graph: a dataset with statistics and a data line with weather data.
 * <p>
 * The labels array represents the labels that are shown on the x-axis. For hour 0 it will display a user friendly text
 * with the day, a date in a short format and the hour. The hours thereafter will be represented with just the hour in
 * 24H format.
 * <p>
 * The tempData and visitData represent the temperatures for that hour and the amount of visits. Other fields are for
 * convenience to determine if the page has a previous page and a next page.
 *
 * @author Bas Piepers
 */
@DataObject
public class StatisticsPageDto {

    private final JsonArray labels;
    private final JsonArray tempData;
    private final JsonArray visitData;
    private final boolean hasNext;
    private final boolean hasPrevious;

    public StatisticsPageDto(JsonArray labels, JsonArray tempData, JsonArray visitData, boolean hasNext, boolean hasPrevious) {
        this.labels = labels;
        this.tempData = tempData;
        this.visitData = visitData;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    public StatisticsPageDto(JsonObject jsonObject) {
        this.labels = jsonObject.getJsonArray("labels");
        this.tempData = jsonObject.getJsonArray("tempData");
        this.visitData = jsonObject.getJsonArray("visitData");
        this.hasNext = jsonObject.getBoolean("hasNext");
        this.hasPrevious = jsonObject.getBoolean("hasPrevious");
    }

    public JsonArray getLabels() {
        return labels;
    }

    public JsonArray getTempData() {
        return tempData;
    }

    public JsonArray getVisitData() {
        return visitData;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatisticsPageDto that = (StatisticsPageDto) o;

        if (hasNext != that.hasNext) return false;
        if (hasPrevious != that.hasPrevious) return false;
        if (!labels.equals(that.labels)) return false;
        if (!tempData.equals(that.tempData)) return false;
        return visitData.equals(that.visitData);

    }

    @Override
    public int hashCode() {
        int result = labels.hashCode();
        result = 31 * result + tempData.hashCode();
        result = 31 * result + visitData.hashCode();
        result = 31 * result + (hasNext ? 1 : 0);
        result = 31 * result + (hasPrevious ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StatisticsPageDto{" +
                "labels=" + labels +
                ", tempData=" + tempData +
                ", visitData=" + visitData +
                ", hasNext=" + hasNext +
                ", hasPrevious=" + hasPrevious +
                '}';
    }
}
