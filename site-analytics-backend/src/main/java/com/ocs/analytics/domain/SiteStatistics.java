package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Wrapper for {@link SiteStatistic} that represents the file that was imported and the enriched {@link SiteStatistic}
 * records that contain the weather measurements. This object will be returned as a response to importing a site-
 * statistics file.
 *
 * @author Bas Piepers
 */
@DataObject
public class SiteStatistics implements JsonDomainObject, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteStatistics.class);

    private final Set<SiteStatistic> statistics;

    public SiteStatistics(TreeSet<SiteStatistic> statistics) {
        this.statistics = statistics;
    }

    public SiteStatistics() {
        this.statistics = new TreeSet<>();
    }

    public SiteStatistics(JsonObject jsonObject) {
        this.statistics = jsonObject
                .getJsonArray("statistics")
                .stream()
                .map(o -> new SiteStatistic((JsonObject) o))
                .collect(Collectors.toCollection(() -> new TreeSet<>()));
    }

    public Set<SiteStatistic> getStatistics() {
        return statistics;
    }

    public List<SiteStatistic> getAsSortedList() {
        return statistics
                .stream()
                .sorted()
                .collect(Collectors
                        .toList());
    }

    public SiteStatistics addStatistic(SiteStatistic siteStatistic) {
        this.statistics.add(siteStatistic);
        return this;
    }

    /**
     * Add an instance of statistics based on the incoming csv.
     *
     * @param csv, the csv record of which we assume that it contains certain columns in certain formats.
     */
    public void addStatisticsFromCsv(String csv) {
        this.statistics.add(SiteStatistic.from(csv));

    }

    /**
     * Based on a CSV record, extract the hour-of-day and find the corresponding {@link SiteStatistic}. Map the CSV
     * record to a {@link WeatherMeasurement} instance and add it to the {@link SiteStatistic}. If no corresponding
     * {@link SiteStatistic} was found, create one with 0 values and add the weathermeasurement to it.
     * <p>
     * Naive implementation: probably better to allow a lookup by the time portions rather than iterating the Set over
     * and over again.
     *
     * @param csv, the csv record as obtained from a web site with weather data of a weather station.
     * @return an instance of this class for fluent API building.
     */
    public SiteStatistics addMeasurementBasedOnRecord(String csv) {
        String[] contents = csv.split(",");
        WeatherMeasurement weatherMeasurement = WeatherMeasurement.from(contents);
        // Expecting year-month-day in second position
        String date = contents[1];

        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(4, 6));
        int day = Integer.valueOf(date.substring(6));

        // Expecting the third position to contain a hour of day.
        int hour = (Integer.valueOf(contents[2])) - 1;

        HourOfDay hourOfDay = HourOfDay.of(year, month, day, hour);

        // Find the corresponding SiteStatistic (if not found, we assume that no statistics exist) based on the hour of day (not all hours may be present) taking the difference in hour notation into account
        Optional<SiteStatistic> s = this.statistics
                .stream()
                .filter(siteStatistic -> siteStatistic
                        .getHourOfDay()
                        .equals(hourOfDay))
                .findFirst();

        if (s.isPresent()) {
            s.get().weatherMeasurement(weatherMeasurement);
        } else {
            this.statistics.add(SiteStatistic.ofZeroWithWeatherData(hourOfDay, weatherMeasurement));
        }
        return this;
    }

    /**
     * Convenience method to validate whether the statistics do not span a longer period than one year (12 months).
     * Note: will also return true if no statistics exist yet. Uses {@link LocalDate} and {@link Period} to determine
     * these rules. Does not take timezone difference into account.
     *
     * @return true if the statistics are populated and do not span more than a year.
     */
    public boolean spansMoreThanAYear() {

        boolean result = false;
        if (!this.statistics.isEmpty()) {
            SiteStatistic first = ((TreeSet<SiteStatistic>) this.statistics).first();
            SiteStatistic last = ((TreeSet<SiteStatistic>) this.statistics).last();

            // Don't continue if the period spans more than a year.
            LocalDate start = LocalDate.of(first.getHourOfDay().getYear().getValue(),
                    first.getHourOfDay().getMonth().getValue(),
                    first.getHourOfDay().getDay().getValue());

            LocalDate end = LocalDate.of(last.getHourOfDay().getYear().getValue(),
                    last.getHourOfDay().getMonth().getValue(),
                    last.getHourOfDay().getDay().getValue());

            Period p = Period.between(start, end);
            int days = p.getDays();
            long months = p.toTotalMonths();
            if (months > 12 || (months == 12 && days > 0)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * Gets the first record in the {@link TreeSet} if its not empty. Casts it to a {@link TreeSet} because we want to use
     * {@link TreeSet} in this class but Vert.x doesn't allow the field to be of that type.
     *
     * @return the first record of the {@link TreeSet}
     */
    public SiteStatistic first() {
        SiteStatistic result = null;
        if (!statistics.isEmpty()) {
            result = ((TreeSet<SiteStatistic>) this.statistics).first();
        }

        return result;
    }


    /**
     * Gets the last record in the {@link TreeSet} if it is not empty. Casts it to a {@link TreeSet} because we want to
     * use {@link TreeSet} in this class but Vert.x doesn't allow the field to be of that type.
     *
     * @return the last record of the {@link TreeSet}
     */
    public SiteStatistic last() {
        SiteStatistic result = null;
        if (!statistics.isEmpty()) {
            result = ((TreeSet<SiteStatistic>) this.statistics).last();
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteStatistics that = (SiteStatistics) o;

        return statistics.equals(that.statistics);
    }

    @Override
    public int hashCode() {
        return statistics.hashCode();
    }

    @Override
    public String toString() {
        return "SiteStatistics{" +
                "statistics=" + statistics +
                '}';
    }
}
