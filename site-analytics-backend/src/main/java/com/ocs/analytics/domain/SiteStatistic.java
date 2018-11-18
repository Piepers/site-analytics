package com.ocs.analytics.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents one statistic record of an imported file of a website enriched with (historical-) weather measurements. Is
 * capable of mapping from CSV to an instance of itself.
 *
 * @author Bas Piepers
 */
@DataObject
public class SiteStatistic implements Comparable<SiteStatistic> {
    private static final String EXPECTED_FORMAT = "yyyyMMddHH";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EXPECTED_FORMAT);

    private final String id;
    @JsonUnwrapped(prefix = "year.")
    private final Year year;
    @JsonUnwrapped(prefix = "month.")
    private final Month month;
    @JsonUnwrapped(prefix = "day.")
    private final DayOfMonth day;
    @JsonUnwrapped(prefix = "hour.")
    private final HourOfDay hour;
    private final Long users;
    private final Long newUsers;
    private final Long sessions;
    private List<WeatherMeasurement> weatherMeasurements;


    public SiteStatistic(String id, Year year, Month month, DayOfMonth day, HourOfDay hour, Long users, Long newUsers, Long sessions) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.users = users;
        this.newUsers = newUsers;
        this.sessions = sessions;
    }

    public SiteStatistic(JsonObject jsonObject) {
        this.id = jsonObject.getString("id");
        this.year = Year.of(jsonObject.getInteger("year.value"));
        this.month = Month.of(jsonObject.getInteger("month.value"));
        this.day = DayOfMonth.of(jsonObject.getInteger("day.value"));
        this.hour = HourOfDay.of(jsonObject.getInteger("hour.value"));

        this.users = jsonObject.getLong("users");
        this.newUsers = jsonObject.getLong("newUsers");
        this.sessions = jsonObject.getLong("sessions");
        this.weatherMeasurements = Objects.nonNull(jsonObject.getJsonArray("weatherMeasurements")) ? jsonObject.getJsonArray("weatherMeasurements")
                .stream()
                .map(element -> new WeatherMeasurement((JsonObject) element))
                .collect(Collectors.toList()) : null;
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

        return new SiteStatistic(UUID.randomUUID().toString(), Year.of(ldt.getYear()), Month.of(ldt.getMonthValue()),
                DayOfMonth.of(ldt.getDayOfMonth()), HourOfDay.of(ldt.getHour()), users, newUsers, sessions);

    }

    public String getId() {
        return id;
    }

    public Year getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public DayOfMonth getDay() {
        return day;
    }

    public HourOfDay getHour() {
        return hour;
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

    public List<WeatherMeasurement> getWeatherMeasurements() {
        return weatherMeasurements;
    }

    @Override
    public String toString() {
        return "SiteStatistic{" +
                "id='" + id + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", users=" + users +
                ", newUsers=" + newUsers +
                ", sessions=" + sessions +
                ", weatherMeasurements=" + weatherMeasurements +
                '}';
    }

    @Override
    public int compareTo(SiteStatistic o) {
        return COMPARATOR.compare(this, o);
    }

    // Note: this is slower than an old nested if construct.
    private static final Comparator<SiteStatistic> COMPARATOR = Comparator
            .comparingInt((SiteStatistic s) -> s.hour.getValue())
            .thenComparingInt(s -> s.day.getValue())
            .thenComparingInt(s -> s.month.getValue())
            .thenComparingInt(s -> s.year.getValue());

}
