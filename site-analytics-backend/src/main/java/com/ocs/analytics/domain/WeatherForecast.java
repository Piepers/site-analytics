package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A weather forecast contains 5 days worth of data with a 3 hour interval of forecast data for a particular city.
 *
 * @author Bas Piepers
 */
@DataObject
public class WeatherForecast {
    private final City city;
    private final List<WeatherItem> items;

    public WeatherForecast(City city, List<WeatherItem> items) {
        this.city = city;
        this.items = items;
    }

    public WeatherForecast(JsonObject jsonObject) {
        this.city = new City(jsonObject.getJsonObject("city"));
        this.items = jsonObject
                .getJsonArray("items")
                .stream()
                .map(o -> new WeatherItem((JsonObject) o))
                .collect(Collectors.toList());
    }

    public City getCity() {
        return city;
    }

    public List<WeatherItem> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherForecast that = (WeatherForecast) o;

        if (!city.equals(that.city)) return false;
        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        int result = city.hashCode();
        result = 31 * result + items.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "WeatherForecast{" +
                "city=" + city +
                ", items=" + items +
                '}';
    }
}
