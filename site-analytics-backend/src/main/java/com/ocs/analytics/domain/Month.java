package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

@DataObject
public class Month implements Serializable {
    private int value;

    public Month(JsonObject jsonObject) {
        this.value = jsonObject.getInteger("month");
    }

    private Month(int value) {
        this.value = value;
    }

    public static final Month of(int value) {
        if (value > 12 || value < 1) {
            throw new IllegalArgumentException("Invalid value for month (" + value + ").");
        }
        return new Month(value);
    }

    public java.time.Month asTimeMonth() {
        return java.time.Month.of(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Month month = (Month) o;

        return value == month.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "Month{" +
                "value=" + value +
                '}';
    }
}
