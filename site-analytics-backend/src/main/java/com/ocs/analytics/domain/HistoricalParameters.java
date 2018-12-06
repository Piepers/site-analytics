package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.MultiMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Contains the parameters that we derive from the analytics import to determine with which parameters we must query the
 * the historical data of a weather station.
 *
 * @author Bas Piepers
 */
@DataObject
// TODO: validate that start and end is not more than a year and make objects of the fields and/or work with java time.
public class HistoricalParameters {
    private final String language;
    //    private final Year startYear;
//    private final Year endYear;
//    private final Month startMonth;
//    private final Month endMonth;
//    private final MonthDay startDay;
//    private final MonthDay endDay;
//    private final
    private final int startYear;
    private final int startMonth;
    private final int startDay;
    private final int endYear;
    private final int endMonth;
    private final int endDay;
    private final int startHour;
    private final int endHour;
    private List<String> variables;
    private final String stations;
    private static final String SUBMIT = "Download dataset";

    public HistoricalParameters(JsonObject jsonObject) {
        this.language = jsonObject.getString("language");
        this.startYear = jsonObject.getInteger("startYear");
        this.startMonth = jsonObject.getInteger("startMonth");
        this.startDay = jsonObject.getInteger("startDay");
        this.endYear = jsonObject.getInteger("endYear");
        this.endMonth = jsonObject.getInteger("endMonth");
        this.endDay = jsonObject.getInteger("endDay");
        this.startHour = jsonObject.getInteger("startHour");
        this.endHour = jsonObject.getInteger("endHour");
        this.variables = jsonObject.getJsonArray("variables", new JsonArray())
                .stream()
                .map(variable -> new String((String) variable)).collect(Collectors.toList());
        this.stations = jsonObject.getString("stations");

        this.validate();

    }

    /**
     * A constructor with some validation.
     */
    private HistoricalParameters(String language, int startYear, int startMonth, int startDay, int endYear, int endMonth,
                                 int endDay, int startHour, int endHour, String stations, String... variables) {

        this.language = language;
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDay = endDay;
        this.startHour = startHour;
        this.endHour = endHour;
        this.stations = stations;

        if (Objects.nonNull(variables)) {
            this.variables = Arrays.stream(variables).collect(Collectors.toList());
        }
    }

    /**
     * Assumes that the hours are starting at 0 for midnight and end at 23 and so it will add one hour because the
     * web site where the values are taken from has a 1 to 24 hour format.
     */
    public static final HistoricalParameters with(String language, int startYear, int startMonth, int startDay, int endYear, int endMonth,
                                                  int endDay, int startHour, int endHour, String stations, String... variables) {
        HistoricalParameters h = new HistoricalParameters(language, startYear, startMonth, startDay, endYear, endMonth, endDay, startHour + 1, endHour + 1, stations, variables);

        h.validate();

        return h;

    }

    /**
     * Used to map the fields of this object to something we can put in a form in the
     * {@link io.vertx.core.http.HttpClient}
     *
     * @return a multimap with the contents of the fields of this object.
     */
    public MultiMap asMultiMapForForm() {
        MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        multiMap.add("lang", this.language)
                .add("byear", String.valueOf(this.startYear))
                .add("bmonth", String.valueOf(this.startMonth))
                .add("bday", String.valueOf(this.startMonth))
                .add("eyear", String.valueOf(this.endYear))
                .add("emonth", String.valueOf(this.endMonth))
                .add("eday", String.valueOf(this.endDay))
                .add("bhour", String.valueOf(this.startHour))
                .add("ehour", String.valueOf(this.endHour))
                .add("stations", this.stations)
                .add("submit", SUBMIT);

        if (Objects.nonNull(this.variables) && !this.variables.isEmpty()) {
            this.variables
                    .stream()
                    .forEach(var -> multiMap
                            .add("variabele", var));
        }

        return multiMap;
    }

    private void validate() {
        if (this.startYear < 1900 || this.startYear > endYear) {
            throw new IllegalStateException("The start year can never be greater than the end year and must be after 1900");
        }

        if (this.endYear < this.startYear) {
            throw new IllegalStateException("Ths end year can never be before the start year.");
        }

        if (this.startMonth < 1 || this.startMonth > 12 || (this.startYear == this.endYear &&
                this.startMonth > this.endMonth) || this.endMonth < 1 || this.endMonth > 12) {
            throw new IllegalStateException("The start- and end-month must have a valid value (1 - 12) and can never be after the end month.");
        }

        // We do not validate for Gregorian Calendar months (whether a month has 28, 29, 30 or 31 days)
        if (this.startDay < 1 || this.startDay > 31 || (this.startYear == this.endYear && this.startMonth == this.endMonth
                && this.startDay > this.endDay) || this.endDay < 1 || this.endDay > 31) {
            throw new IllegalStateException("The start day must be a valid day and must not be after the end day.");
        }

        if (this.startHour < 1 || this.startHour > 24 || this.endHour < 1 || this.endHour > 24 ||
                (this.startYear == this.endYear && this.startMonth == this.endMonth && this.startDay == this.endDay &&
                        this.endHour < this.startHour)) {
            throw new IllegalStateException("The start- and end-hour must contain valid values and the end-hour can never be before the start-hour.");
        }
    }

    public String getLanguage() {
        return language;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public int getEndDay() {
        return endDay;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public List<String> getVariables() {
        return variables;
    }

    public String getStations() {
        return stations;
    }

    public void addVariable(String variable) {
        if (Objects.isNull(this.variables)) {
            this.variables = new ArrayList<>();
        }

        this.variables.add(variable);
    }

    @Override
    public String toString() {
        return "HistoricalParameters{" +
                "language='" + language + '\'' +
                ", startYear=" + startYear +
                ", startMonth=" + startMonth +
                ", startDay=" + startDay +
                ", endYear=" + endYear +
                ", endMonth=" + endMonth +
                ", endDay=" + endDay +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                ", variables=" + variables +
                ", stations='" + stations + '\'' +
                '}';
    }
}
