package com.ocs.analytics.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents one statistic record of an imported file of a website enriched with (historical-) weather measurements. Is
 * capable of mapping from CSV to an instance of itself.
 * <p>
 * SiteStatistic implements Comparable because we would like to be sure that we can sort it by the date
 * (the hour of the day).
 *
 * @author Bas Piepers
 */
@DataObject
public class SiteStatistic implements Comparable<SiteStatistic> {
    private static final String EXPECTED_FORMAT = "yyyyMMddHH";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EXPECTED_FORMAT);

    private final String id;
    @JsonUnwrapped
    private final HourOfDay hourOfDay;
    private final Long users;
    private final Long newUsers;
    private final Long sessions;
    // The weather measurements of one hour.
    private WeatherMeasurement weatherMeasurements;


    public SiteStatistic(String id, HourOfDay hourOfDay, Long users, Long newUsers, Long sessions) {
        this.id = id;
        this.hourOfDay = hourOfDay;
        this.users = users;
        this.newUsers = newUsers;
        this.sessions = sessions;
    }

    public SiteStatistic(JsonObject jsonObject) {
        this.id = jsonObject.getString("id");
        this.hourOfDay = new HourOfDay(jsonObject);
        this.users = jsonObject.getLong("users");
        this.newUsers = jsonObject.getLong("newUsers");
        this.sessions = jsonObject.getLong("sessions");
        this.weatherMeasurements = Objects.nonNull(jsonObject.getJsonObject("weatherMeasurement")) ? new WeatherMeasurement(jsonObject.getJsonObject("weatherMeasurements")) : null;
    }

    /**
     * Maps one line of CSV from the site-statistics import to an instance of this class. Does assumptions on the
     * contents of the columns in the CSV.
     *
     * @param csv, a line from a csv file that has certain columns.
     * @return an instance of this class with time data and site statistics.
     */
    public static SiteStatistic from(String csv) {
        String contents[] = csv.split(",");

        if (contents.length != 5) {
            throw new IllegalArgumentException("Expected a csv string of 5 columns.");
        }

        String yearMonthDayHour = contents[0];
        LocalDateTime ldt = LocalDateTime.parse(yearMonthDayHour, formatter);
        Long users = Long.valueOf(contents[1]);
        Long newUsers = Long.valueOf(contents[2]);
        Long sessions = Long.valueOf(contents[3]);

        return new SiteStatistic(UUID.randomUUID().toString(), new HourOfDay(Year.of(ldt.getYear()), Month.of(ldt.getMonthValue()),
                Day.of(ldt.getDayOfMonth()), Hour.of(ldt.getHour())), users, newUsers, sessions);
    }

    public String getId() {
        return id;
    }

    public HourOfDay getHourOfDay() {
        return hourOfDay;
    }

    public Long getUsers() {
        return users;
    }

    public Long getNewUsers() {
        return newUsers;
    }

    public Long getSessions() {
        return sessions;
    }

    /**
     * Some convenience getters for temporal fields
     *
     * @return
     */
    public int year() {
        return this.hourOfDay.getYear().getValue();
    }

    public int month() {
        return this.hourOfDay.getMonth().getValue();
    }

    public int day() {
        return this.hourOfDay.getDay().getValue();
    }

    public int hour() {
        return this.hourOfDay.getHour().getValue();

    }

    public WeatherMeasurement getWeatherMeasurements() {
        return weatherMeasurements;
    }

    /**
     * Set the weather measurement element for this site statistic.
     *
     * @param weatherMeasurements, the {@link WeatherMeasurement} for this instance.
     * @return an instance of this class for fluent API building.
     */
    public SiteStatistic weatherMeasurement(WeatherMeasurement weatherMeasurements) {
        this.weatherMeasurements = weatherMeasurements;
        return this;
    }

    // Note: this is slower than an old nested if construct but makes it more readable.
    private static final Comparator<SiteStatistic> COMPARATOR = Comparator
            .comparingInt((SiteStatistic ss) -> ss.hourOfDay.getHour().getValue())
            .thenComparingInt(ss -> ss.hourOfDay.getDay().getValue())
            .thenComparingInt(ss -> ss.hourOfDay.getMonth().getValue())
            .thenComparingInt(ss -> ss.hourOfDay.getYear().getValue());

    @Override
    public int compareTo(SiteStatistic o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteStatistic that = (SiteStatistic) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "SiteStatistic{" +
                "id='" + id + '\'' +
                ", hourOfDay=" + hourOfDay +
                ", users=" + users +
                ", newUsers=" + newUsers +
                ", sessions=" + sessions +
                ", weatherMeasurements=" + weatherMeasurements +
                '}';
    }
}
