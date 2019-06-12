package com.ocs.analytics.application;

import com.ocs.analytics.domain.SiteStatistic;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;

/**
 * The representation of the sitestatistics as it is used by the client application (aka. webpage) with the ability to
 * paginate the data. This object is stored in the session and the client can cycle the statistics data. The pages
 * are split up so that they contain data of a maximum amount of three days and the user can cycle through them per
 * day.
 * <p>
 * This class contains a {@link HashMap} for a reasonable performance and relies on the start and end fields to determine how
 * much records are present. The key of the map is an Integer since that is significantly faster than using an Instant or
 * the HourOfDay even with transforming the fields. This integer contains the year, month and day as a value (eg.
 * 20180101 for the 1st of January 2018) and the values are the hours in that day and the data per hour.
 *
 * @author Bas Piepers
 */

public class SiteStatisticsDto implements Serializable {
    // Amount of days a page contains.
    private static final int PAGE_SIZE = 3;
    // Amount of days an increment shifts (next/previous)
    private static final int INCREMENT_SIZE = 1;
    private static final int ONE_DAY_MS = 86400000;
    // Where we are at the moment. If null assume start of pages.
    private Integer currentStartKey;
    // The first day in the Map (an integer like 20180101 for the first of january 2018).
    private Integer startKey;
    // The last day in the Map (an integer like 20181231 for the 31st of december 2018).
    private Integer endKey;

    private Map<Integer, OneDayStatisticsDto> statistics = new HashMap<>();

    private SiteStatisticsDto(Integer start, Integer end, TreeMap<Integer, OneDayStatisticsDto> statistics) {
        this.startKey = start;
        this.endKey = end;
        this.statistics = statistics;
    }

    /**
     * Converts a {@link SiteStatistic} set to a representation that can be used by the front-end. Relies on the fact
     * that the given map is ordered by the {@link com.ocs.analytics.domain.HourOfDay} field of the {@link SiteStatistic}
     * class.
     *
     * @param orderedStatistics, the collection of records, ordered by the {@link com.ocs.analytics.domain.HourOfDay}
     *                           field of the {@link SiteStatistic}.
     * @return an instance of this class with information that can be used by the front-end.
     */
    public static SiteStatisticsDto fromOrderedStatistics(TreeSet<SiteStatistic> orderedStatistics) {
        // TODO: check if it is faster to iterate the set and get the first and last instants as we go.
        SiteStatistic first = orderedStatistics.first();
        Integer startKey = first.getHourOfDay().yearMonthDayAsFormattedInteger();
        // TODO: the last record can be in the middle of the day somewhere so shifting back and forth should account for that.
        SiteStatistic last = orderedStatistics.last();
        Integer endKey = last.getHourOfDay().yearMonthDayAsFormattedInteger();

        TreeMap<Integer, OneDayStatisticsDto> dtoMap = orderedStatistics
                .stream()
                .collect(groupingBy(siteStatistic -> siteStatistic
                                .getHourOfDay()
                                .yearMonthDayAsFormattedInteger(),// Group per day
                        TreeMap::new,// Collect into a treemap (instead of the default HashMap)
                        collectingAndThen(Collectors.toCollection(() -> new TreeSet<>()),// Collect into a TreeSet and then instantiate the dto (which will map the records with another stream).
                                OneDayStatisticsDto::from)));


        return new SiteStatisticsDto(startKey, endKey, dtoMap);
    }

    /**
     * Start (or shift to the start) and get PAGE_SIZE of OneDayStatisticsDto and return the resulting list.
     *
     * @return the first PAGE_SIZE amount of pages or as much as we have in the map.
     */
    public List<OneDayStatisticsDto> first() {

        return null;
    }

    /**
     * End (or shift to the end) and get PAGE_SIZE of OneDayStatisticsDto and return the resulting list.
     *
     * @return the last PAGE_SIZE amount of pages or as much as we have in the map.
     */
    public List<OneDayStatisticsDto> last() {

        return null;
    }

    /**
     * Get the next INCREMENT_SIZE of {@link OneDayStatisticsDto} objects and return the resulting list (or call "first"
     * if there is no current key yet). If we arrived at the end we keep returning the last page of the map.
     * <p>
     * Works as a queue/linkedlist kind of implementation because the "left" items are dropped (for the amount specified
     * at INCREMENT_SIZE) and the next items are put at the end. INCREMENT_SIZE may be larger than the initial PAGE_SIZE
     * so this method checks where we are and returns the proper amount of dto's.
     *
     * @return the next INCREMENT_SIZE amount of pages. The previous INCREMENT_SIZE items are dropped and the next is put at the
     * end.
     */
    public List<OneDayStatisticsDto> next() {
        // If the current key is null invoke "first"

        // Otherwise: shift INCREMENT_SIZE pages "to the right" and return the resulting list.

        // Unless we are at the end of the list.
        return null;
    }

    /**
     * Get the previous INCREMENT_SIZE of {@link OneDayStatisticsDto} objects and return the resulting list. Returns
     * the first PAGE_SIZE amount of pages if we are already at the beginning (or if the first set was never retrieved
     * before).
     * <p>
     * The implementation is the reverse of the {@link SiteStatisticsDto#next()} method of this class.
     *
     * @return the previous INCREMENT_SIZE amount of pages. The last item in the last INCREMENT_SIZE items are dropped and
     * previous items are put at the front.
     */
    public List<OneDayStatisticsDto> previous() {
        // If the current key is not null and we are not at the start of the list.
        // Shift INCREMENT_SIZE pages "to the left" and return the resulting list.

        return null;
    }

}
