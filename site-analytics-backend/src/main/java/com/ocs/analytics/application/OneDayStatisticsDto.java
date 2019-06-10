package com.ocs.analytics.application;

import com.ocs.analytics.domain.SiteStatistic;
import com.ocs.analytics.domain.WeatherMeasurement;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.TreeSet;

/**
 * One day of site statistics enriched with weather data. This Dto represents what we show in the graph: a dataset with
 * statistics and a data line with weather data.
 * <p>
 * The labels array represents the labels that are shown on the x-axis. For hour 0 it will display a user friendly text
 * with the day, a date in a short format and the hour. The hours thereafter will be represented with just the hour in
 * 24H format.
 * <p>
 * The tempData and visitData represent the temperatures for that hour and the amount of visits. Other fields are for
 * convenience to determine if the page has a previous page and a next page.
 * <p>
 * This class makes sure that a max amount of one day is represented by inspecting the start and end portion of the
 * incoming data.
 *
 * @author Bas Piepers
 */
@DataObject
public class OneDayStatisticsDto {
    private static final Logger LOGGER = LoggerFactory.getLogger(OneDayStatisticsDto.class);
    private static final String LABEL_MIDNIGHT_FORMAT = "dd MMM yyyy HH";
    private static final DateTimeFormatter mnFormatter = DateTimeFormatter.ofPattern(LABEL_MIDNIGHT_FORMAT);
    private final JsonArray labels;
    // Represents the line with the temperature.
    private final JsonArray tempData;
    // Represents the line with amount users.
    private final JsonArray usersData;
    // Represents the line with amount of new users.
    private final JsonArray newUsersData;
    // Represents the line with amount of sessions.
    private final JsonArray sessionsData;

    public OneDayStatisticsDto(JsonArray labels, JsonArray tempData, JsonArray usersData, JsonArray newUsersData,
                               JsonArray sessionsData) {
        this.labels = labels;
        this.tempData = tempData;
        this.usersData = usersData;
        this.newUsersData = newUsersData;
        this.sessionsData = sessionsData;
    }

    public OneDayStatisticsDto(JsonObject jsonObject) {
        this.labels = jsonObject.getJsonArray("labels");
        this.tempData = jsonObject.getJsonArray("tempData");
        this.usersData = jsonObject.getJsonArray("usersData");
        this.newUsersData = jsonObject.getJsonArray("newUsersData");
        this.sessionsData = jsonObject.getJsonArray("sessionsData");
    }


    /**
     * Gets a portion of data which is assumed to start at midnight at a particular day and is assumed to be sorted by
     * hour. This method validates that the amount of data is indeed not more than a day and converts the content
     * into something the front-end can show.
     * <p>
     * Warning: does not validate whether the hourly data is from the same day, only that it does not contain more than
     * 24 records assuming that it contains a proper set in the right order otherwise.
     *
     * @param siteStatistic, the portion of data that is processed. Must start at midnight, can not contain more than
     *                       a day.
     * @return an instance of this class with data that can be displayed in the front-end.
     */
    // TODO: test whether the order is good and/or they are in the correct order and that it is all on the same day?
    public static OneDayStatisticsDto from(TreeSet<SiteStatistic> siteStatistic) {
        Objects.requireNonNull(siteStatistic);

        // We don't process more than a day here
        if (siteStatistic.size() > 24) {
            throw new IllegalArgumentException("Can't process more than a day for one day of statistics.");
        }

        SiteStatistic first = siteStatistic.pollFirst();
        LOGGER.debug("First statistics were of: {} ({})", first.getHourOfDay().toString(), first.getHourOfDay().yearMonthDayAsFormattedInteger());

        if (Objects.isNull(first) || !first.getHourOfDay().isMidnight()) {
            throw new IllegalArgumentException("The site statistics for a page should always start at midnight.");
        }

        String firstLabel = mnFormatter.format(first.getHourOfDay().asLocalDateTime());
        WeatherMeasurement firstWm = first.getWeatherMeasurements();
        Long firstUsers = first.getUsers();
        Long firstSessions = first.getSessions();
        Long firstNewUsers = first.getNewUsers();
        // Default to 0 if no weather data is present for this hour.
        Integer firstTempData = firstWm != null ? firstWm.getTemperature() : 0;
        // Instantiate our arrays and populate the first records.
        JsonArray labels = new JsonArray().add(firstLabel);
        JsonArray tempData = new JsonArray().add(firstTempData);
        JsonArray usersData = new JsonArray().add(firstUsers);
        JsonArray newUsersData = new JsonArray().add(firstNewUsers);
        JsonArray sessionData = new JsonArray().add(firstSessions);

        // Iterate the rest of the set and make sure that we don't surpass a day. Throw an exception if we do.
        siteStatistic
                .stream()
                .forEach(ss -> {
                    int hourOfDay = ss.getHourOfDay().getHour().getValue();
                    labels.add(hourOfDay < 10 ? "0" + hourOfDay : ""+hourOfDay);
                    WeatherMeasurement wm = ss.getWeatherMeasurements();
                    // Default to 0 if there is no weatherdata.
                    tempData.add(wm != null ? wm.getTemperature() : 0);
                    usersData.add(ss.getUsers());
                    newUsersData.add(ss.getNewUsers());
                    sessionData.add(ss.getSessions());
                });

        return new OneDayStatisticsDto(labels, tempData, usersData, newUsersData, sessionData);
    }

    public JsonArray getLabels() {
        return labels;
    }

    public JsonArray getTempData() {
        return tempData;
    }

    public JsonArray getUsersData() {
        return usersData;
    }

    public JsonArray getNewUsersData() {
        return newUsersData;
    }

    public JsonArray getSessionsData() {
        return sessionsData;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OneDayStatisticsDto that = (OneDayStatisticsDto) o;

        if (!labels.equals(that.labels)) return false;
        if (!tempData.equals(that.tempData)) return false;
        if (!usersData.equals(that.usersData)) return false;
        if (!newUsersData.equals(that.newUsersData)) return false;
        return sessionsData.equals(that.sessionsData);
    }

    @Override
    public int hashCode() {
        int result = labels.hashCode();
        result = 31 * result + tempData.hashCode();
        result = 31 * result + usersData.hashCode();
        result = 31 * result + newUsersData.hashCode();
        result = 31 * result + sessionsData.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OneDayStatisticsDto{" +
                "labels=" + labels +
                ", tempData=" + tempData +
                ", usersData=" + usersData +
                ", newUsersData=" + newUsersData +
                ", sessionsData=" + sessionsData +
                '}';
    }
}
