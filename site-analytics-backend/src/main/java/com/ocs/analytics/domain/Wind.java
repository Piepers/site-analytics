package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * A wind item in the weather forecast contains the speed and the defree of the wind.
 *
 * @author Bas Piepers
 *
 */
@DataObject
public class Wind {
    private final Float speed;
    private final Double degree;

    public Wind(Float speed, Double degree) {
        this.speed = speed;
        this.degree = degree;
    }

    public Wind(JsonObject jsonObject) {
        this.speed = jsonObject.getFloat("speed");
        this.degree = jsonObject.getDouble("degree");
    }

    public Float getSpeed() {
        return speed;
    }

    public Double getDegree() {
        return degree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wind wind = (Wind) o;

        if (!speed.equals(wind.speed)) return false;
        return degree.equals(wind.degree);
    }

    @Override
    public int hashCode() {
        int result = speed.hashCode();
        result = 31 * result + degree.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                ", degree=" + degree +
                '}';
    }
}
