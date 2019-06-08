package com.ocs.analytics.application;

import com.ocs.analytics.domain.SiteStatistic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
    private Long currentStartKey;
    // The first day in the Map (an integer like 20180101 for the first of january 2018).
    private Integer startKey;
    // The last day in the Map (an integer like 20181231 for the 31st of december 2018).
    private Integer endKey;

    private Map<Integer, OneDayStatisticsDto> statistics = new HashMap<>();

    private SiteStatisticsDto(Integer start, Integer end, HashMap<Integer, OneDayStatisticsDto> statistics) {
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

//        orderedStatistics.stream().collect(groupingBy())
        // Iterate the set
        // If the hour is midnight add a new key and iterate for that key (nested loop)


        // TODO: grouping per day
//        orderedStatistics
//                .stream()
//                .map(OneDayStatisticsDto::from)
//                .collect(Collectors.toList());// TODO: must be collected in the statistics map grouped by day.
        return null;
    }

}
