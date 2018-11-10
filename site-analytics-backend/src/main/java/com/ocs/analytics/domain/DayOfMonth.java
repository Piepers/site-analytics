package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.time.Month;
import java.time.MonthDay;

@DataObject
public class DayOfMonth {

    private int value;

    public DayOfMonth(JsonObject jsonObject) {
        this.value = jsonObject.getInteger("dayOfMonth");
    }

    private DayOfMonth(int value) {
        this.value = value;
    }

    public static final DayOfMonth of(int value) {
        if (value > 31 || value < 1) {
            throw new IllegalArgumentException("Invalid value for day of month.");
        }
        return new DayOfMonth(value);
    }

    // May throw an exception if the value is too high (eg for months that don't have 31 days while value is 31).
    public MonthDay asTimeDayOfMonth(Month month) {
        return MonthDay.of(month, this.value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DayOfMonth{" +
                "value=" + value +
                '}';
    }
}
