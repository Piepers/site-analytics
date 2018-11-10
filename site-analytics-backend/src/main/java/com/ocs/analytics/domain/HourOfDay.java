package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Represents the hour of day as a Json object. Can never be higher than 24 and must always be above 0. 0 is twelve o'
 * clock midnight.
 *
 * @author Bas Piepers
 */
@DataObject
public class HourOfDay {

    private int value;

    public HourOfDay(JsonObject jsonObject) {
        this.value = jsonObject.getInteger("hourOfDay");
    }

    private HourOfDay(int value) {
        this.value = value;
    }

    public static final HourOfDay of(int value) {
        if (value < 0 || value > 23) {
            throw new IllegalArgumentException("Invalid value for hour of day (" + value + ").");
        }

        return new HourOfDay(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "HourOfDay{" +
                "value=" + value +
                '}';
    }
}
