package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * A weather item is part of the forecast and contains some specifics of the type of weather in a forecast.
 *
 * @author Bas Piepers
 */
@DataObject
public class WeatherItem {
    private final Long id;
    private final String main;
    private final String description;
    private final String icon;

    public WeatherItem(Long id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public WeatherItem(JsonObject jsonObject) {
        this.id = jsonObject.getLong("id");
        this.main = jsonObject.getString("main");
        this.description = jsonObject.getString("description");
        this.icon = jsonObject.getString("icon");
    }

    public Long getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherItem that = (WeatherItem) o;

        if (!id.equals(that.id)) return false;
        if (!main.equals(that.main)) return false;
        if (!description.equals(that.description)) return false;
        return icon.equals(that.icon);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + main.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + icon.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "WeatherItem{" +
                "id=" + id +
                ", main='" + main + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
