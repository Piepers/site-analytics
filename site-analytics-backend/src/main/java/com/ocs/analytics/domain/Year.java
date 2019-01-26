package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * The years that we can pass around as Json objects throughout the system.
 *
 * @author Bas Piepers
 */
@DataObject
public class Year {

    private int value;

    public Year(JsonObject jsonObject) {
        this.value = jsonObject.getInteger("year");
    }

    public static final Year of(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid value for year (" + value + ").");
        }
        return new Year(value);
    }

    private Year(int value) {
        this.value = value;
    }

    public java.time.Year asTimeYear() {
        return java.time.Year.of(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Year year = (Year) o;

        return value == year.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "Year{" +
                "value=" + value +
                '}';
    }
}

