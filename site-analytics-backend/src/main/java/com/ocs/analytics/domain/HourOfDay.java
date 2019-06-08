package com.ocs.analytics.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * The hour, day, month and year in the analytics data. Is used to sort and provide a way to match the statistics data
 * to the data that comes back from the weather station.
 *
 * @author Bas Piepers
 */
@DataObject
public class HourOfDay implements Serializable {
    @JsonUnwrapped(prefix = "year.")
    private Year year;
    @JsonUnwrapped(prefix = "month.")
    private Month month;
    @JsonUnwrapped(prefix = "day.")
    private Day day;
    @JsonUnwrapped(prefix = "hour.")
    private Hour hour;

    public HourOfDay(JsonObject jsonObject) {
        this.year = Year.of(jsonObject.getInteger("year.value"));
        this.month = Month.of(jsonObject.getInteger("month.value"));
        this.day = Day.of(jsonObject.getInteger("day.value"));
        this.hour = Hour.of(jsonObject.getInteger("hour.value"));
    }

    public HourOfDay(Year year, Month month, Day day, Hour hour) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
    }

    public static HourOfDay of(int year, int month, int day, int hour) {
        return new HourOfDay(Year.of(year), Month.of(month), Day.of(day), Hour.of(hour));
    }

    public Year getYear() {
        return year;
    }

    public String yearAsString() {
        return String.valueOf(year.getValue());
    }

    public String monthAsString() {
        return String.valueOf(month.getValue());
    }

    public String dayAsString() {
        return String.valueOf(day.getValue());
    }

    public String hourAsString() {
        return String.valueOf(hour.getValue());
    }

    public Month getMonth() {
        return month;
    }

    public Day getDay() {
        return day;
    }

    public Hour getHour() {
        return hour;
    }

    /**
     * FIXME: We assume that the imported data is from the Dutch timezone.
     * <p>
     * Converts this instance to an instant.
     *
     * @return an instant representation of this class.
     */
    public Instant asInstant() {
        return asLocalDateTime()
                .atZone(ZoneId.of("Europe/Amsterdam"))
                .toInstant();
    }

    public Instant asInstantAtTz(ZoneId zoneId) {
        return asLocalDateTime()
                .atZone(zoneId)
                .toInstant();
    }

    public LocalDateTime asLocalDateTime() {
        return LocalDateTime
                .of(this.year.getValue(),
                        this.month.getValue(),
                        this.day.getValue(),
                        this.hour.getValue(),
                        0);
    }

    public boolean isMidnight() {
        return getHour().getValue() == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HourOfDay hourOfDay = (HourOfDay) o;

        if (!year.equals(hourOfDay.year)) return false;
        if (!month.equals(hourOfDay.month)) return false;
        if (!day.equals(hourOfDay.day)) return false;
        return hour.equals(hourOfDay.hour);
    }

    @Override
    public int hashCode() {
        int result = year.hashCode();
        result = 31 * result + month.hashCode();
        result = 31 * result + day.hashCode();
        result = 31 * result + hour.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "HourOfDay{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                '}';
    }
}
