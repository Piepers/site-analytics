package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.time.Month;
import java.time.MonthDay;

@DataObject
public class Day implements Serializable {

    private int value;

    public Day(JsonObject jsonObject) {
        this.value = jsonObject.getInteger("day");
    }

    private Day(int value) {
        this.value = value;
    }

    public static final Day of(int value) {
        if (value > 31 || value < 1) {
            throw new IllegalArgumentException("Invalid value for day of month.");
        }
        return new Day(value);
    }

    // May throw an exception if the value is too high (eg for months that don't have 31 days while value is 31).
    public MonthDay asTimeDayOfMonth(Month month) {
        return MonthDay.of(month, this.value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Day day = (Day) o;

        return value == day.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "Day{" +
                "value=" + value +
                '}';
    }
}
