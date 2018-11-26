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
public class Hour {

    private int value;

    public Hour(JsonObject jsonObject) {
        this.value = jsonObject.getInteger("hour");
    }

    private Hour(int value) {
        this.value = value;
    }

    public static final Hour of(int value) {
        if (value < 0 || value > 23) {
            throw new IllegalArgumentException("Invalid value for hour of day (" + value + ").");
        }

        return new Hour(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Hour{" +
                "value=" + value +
                '}';
    }
}
