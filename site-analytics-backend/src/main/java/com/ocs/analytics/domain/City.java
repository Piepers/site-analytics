package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * A city is needed to store information about where we ask Weather forecasts for.
 *
 * @author Bas Piepers
 */
// TODO: make this available in the configuration for now but get this from persistent storage later.
@DataObject
public class City {
    private final Long id;
    private final String name;
    private final String country;

    public City(Long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public City(JsonObject jsonObject) {
        this.id = jsonObject.getLong("id");
        this.name = jsonObject.getString("name");
        this.country = jsonObject.getString("country");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return id.equals(city.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
