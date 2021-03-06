package com.ocs.analytics.application;

import com.ocs.analytics.domain.SiteStatistic;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;

/**
 * The representation of the sitestatistics as it is used by the client application (aka. webpage) with the ability to
 * paginate the data. This object is stored in the session and the client can cycle the statistics data. The pages
 * are split up so that they contain data of a maximum amount of days and the user can cycle through them per
 * day (or whatever is in INCREMENT_SIZE).
 * <p>
 * This class stores its data in a {@link HashMap} for a reasonable performance and relies on the start, end and current
 * keys to determine where we are in the pagination and how much is present.
 * <p>
 * The key of the map is an Integer since that is significantly faster than using an Instant or the HourOfDay even with
 * transforming the fields. This integer contains the year, month and day as a value (eg 20180101 for the 1st of January
 * 2018) and the values are the hours in that day and the data per hour.
 * <p>
 * The keys are stored as {@link java.time.LocalDate} so that we can easily increment/decrement the days and parse it
 * into a format that we can use to get the corresponding day content.
 *
 * @author Bas Piepers
 */

public class SiteStatisticsDto implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteStatisticsDto.class);
    // Amount of days a page contains.
    private static final int PAGE_SIZE = 3;
    // Amount of days an increment shifts (next/previous)
    private static final int INCREMENT_SIZE = 1;
    private static final String KEY_FORMAT = "yyyyMMdd";
    private static final DateTimeFormatter keyFormatter = DateTimeFormatter.ofPattern(KEY_FORMAT);
    // Where the start is at the moment. If null assume start of pages.
    private LocalDate sop;
    // Where the current end is at the moment. If null assume start of page + increment size.
    private LocalDate eop;
    // The first day in the Map.
    private LocalDate startKey;
    // The last day in the Map .
    private LocalDate endKey;

    // The entire collection of statistics as we imported it.
    private Map<Integer, OneDayStatisticsDto> statistics;
    // The current page.
    private LinkedList<OneDayStatisticsDto> currentPage;

    private SiteStatisticsDto(LocalDate start, LocalDate end, HashMap<Integer, OneDayStatisticsDto> statistics) {
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
        SiteStatistic first = orderedStatistics.first();
        LocalDate startKey = first.getHourOfDay().asLocalDate();
        // TODO: the last record can be in the middle of the day somewhere so shifting back and forth should account for that.
        SiteStatistic last = orderedStatistics.last();
        LocalDate endKey = last.getHourOfDay().asLocalDate();

        HashMap<Integer, OneDayStatisticsDto> dtoMap = orderedStatistics
                .stream()
                .collect(groupingBy(siteStatistic -> siteStatistic
                                .getHourOfDay()
                                .yearMonthDayAsFormattedInteger()// Group per day
                        , HashMap::new,
//                        ,TreeMap::new,// Collect into a treemap (instead of the default HashMap)
                        collectingAndThen(Collectors.toCollection(() -> new TreeSet<>()),// Collect the grouped content into a TreeSet and...
                                OneDayStatisticsDto::from)));// ...instantiate the dto (which will map the records with another stream).


        return new SiteStatisticsDto(startKey, endKey, dtoMap);
    }

    /**
     * Start (or shift to the start) and get PAGE_SIZE of OneDayStatisticsDto and return the resulting list.
     *
     * @return the first PAGE_SIZE amount of pages or as much as we have in the map.
     */
    public List<OneDayStatisticsDto> first() {
        this.currentPage = new LinkedList<>();
        this.sop = this.startKey;
        // Set the end of page key to the start op page + increment or the end op the collection if it happens to be after that.
        this.eop = (this.eop = sop.plusDays(PAGE_SIZE - 1)).isAfter(this.endKey) ? this.endKey : this.eop;
        return fillPageStatistics();
    }


    /**
     * End (or shift to the end) and get PAGE_SIZE of OneDayStatisticsDto and return the resulting list.
     *
     * @return the last PAGE_SIZE amount of pages or as much as we have in the map.
     */
    public List<OneDayStatisticsDto> last() {
        this.currentPage = new LinkedList<>();
        this.eop = this.endKey;
        // Set the start of the page to the start page - increment or the start of the collection of it happens to be before that.
        this.sop = (this.sop = eop.minusDays(PAGE_SIZE - 1)).isBefore(this.startKey) ? this.startKey : this.sop;

        return fillPageStatistics();
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
        // If the current page is not set yet, assume we must call first.
        if (Objects.isNull(this.currentPage)) {
            return this.first();
        }

        // If we're already at the end, clear the list and return it.
        if (this.sop.isEqual(endKey)) {
            this.currentPage.clear();
            return this.currentPage;
        }

        // Set the sop and eop.
        this.sop = (this.sop = this.sop.plusDays(INCREMENT_SIZE)).isAfter(endKey) ? this.endKey : this.sop;
        LocalDate ld = this.eop;
        this.eop = (this.eop = this.eop.plusDays(INCREMENT_SIZE)).isAfter(endKey) ? this.endKey : this.eop;
        // Process next, always remove the first item until it is empty.
        try {
            int count = 0;
            while (count < INCREMENT_SIZE) {
                this.currentPage.removeFirst();
                count += 1;
            }
        } catch (NoSuchElementException e) {
            LOGGER.debug("We are already at the end of the list and the page is also empty. Page contains {} items.", this.currentPage.size());
        }

        // Fill the page with the next content.
        OneDayStatisticsDto dto;
        ld = ld.plusDays(1);
        while (!ld.isAfter(eop) && (dto = this.statistics.get(this.formatDateToKey(ld))) != null) {
            ld = ld.plusDays(1);
            this.currentPage.add(dto);
        }
        return this.currentPage;
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
        // If we do not have a page yet return null.
        if (Objects.isNull(this.currentPage)) {
            LOGGER.debug("Previous called without pages.");
            return null;
        }

        // If we are at the start again, clear the list and return it.
        if (this.eop.isEqual(startKey)) {
            this.currentPage.clear();
            return this.currentPage;
        }

        // Set the sop and eop.
        LocalDate ld = this.sop;
        this.sop = (this.sop = this.sop.minusDays(INCREMENT_SIZE)).isBefore(startKey) ? this.startKey : this.sop;
        this.eop = (this.eop = this.eop.minusDays(INCREMENT_SIZE)).isBefore(startKey) ? this.startKey : this.eop;

        // Process previous, always remove the last item until it is empty.
        try {
            int count = 0;
            while (count < INCREMENT_SIZE) {
                this.currentPage.removeLast();
                count += 1;
            }
        } catch (NoSuchElementException e) {
            LOGGER.debug("We are already at the start of the list and the page is also empty. Page contains {} items.", this.currentPage.size());
        }

        // Fill the page with the previous content.
        OneDayStatisticsDto dto;
        ld = ld.minusDays(1);
        while (!ld.isBefore(sop) && (dto = this.statistics.get(this.formatDateToKey(ld))) != null) {
            ld = ld.minusDays(1);
            this.currentPage.addFirst(dto);
        }
        return this.currentPage;
    }

    public JsonObject getPageAsJson() {
        if (Objects.isNull(this.currentPage)) {
            LOGGER.debug("No page available yet. Call 'first()'.");
            return new JsonObject();
        }
        JsonArray result = new JsonArray(this.currentPage);
        return new JsonObject()
                .put("count", statistics.size())
                .put("startKey", keyFormatter.format(this.startKey))
                .put("endKey", keyFormatter.format(this.endKey))
                .put("sop", keyFormatter.format(this.sop))
                .put("eop", keyFormatter.format(this.eop))
                .put("page", result);
    }

    private List<OneDayStatisticsDto> fillPageStatistics() {
        LocalDate ld = sop;
        OneDayStatisticsDto dto;
        while (!ld.isAfter(eop) && (dto = this.statistics.get(this.formatDateToKey(ld))) != null) {
            this.currentPage.add(dto);
            ld = ld.plusDays(1);
        }
        return this.currentPage;
    }

    private Integer formatDateToKey(LocalDate localDate) {
        return Integer.valueOf(localDate.format(keyFormatter));
    }
}
