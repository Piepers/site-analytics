package com.ocs.analytics.application;

import com.ocs.analytics.domain.HourOfDay;
import com.ocs.analytics.domain.SiteStatistic;
import org.junit.jupiter.api.Test;

import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OneDayStatisticsDtoTest {

    private OneDayStatisticsDto dto;

    @Test
    public void test_that_exception_is_thrown_when_site_statistics_are_null() {
        assertThatThrownBy(() -> OneDayStatisticsDto.from(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void test_that_when_site_statics_source_are_more_than_one_day_that_exception_is_thrown() {
        // Given
        TreeSet<SiteStatistic> longerThanADay = new TreeSet<>();
        longerThanADay.add(new SiteStatistic("1", HourOfDay.of(2018, 1, 1, 0), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("2", HourOfDay.of(2018, 1, 1, 1), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("3", HourOfDay.of(2018, 1, 1, 2), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("4", HourOfDay.of(2018, 1, 1, 3), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("5", HourOfDay.of(2018, 1, 1, 4), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("6", HourOfDay.of(2018, 1, 1, 5), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("7", HourOfDay.of(2018, 1, 1, 6), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("8", HourOfDay.of(2018, 1, 1, 7), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("9", HourOfDay.of(2018, 1, 1, 8), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("10", HourOfDay.of(2018, 1, 1, 9), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("11", HourOfDay.of(2018, 1, 1, 10), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("12", HourOfDay.of(2018, 1, 1, 11), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("13", HourOfDay.of(2018, 1, 1, 12), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("14", HourOfDay.of(2018, 1, 1, 13), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("15", HourOfDay.of(2018, 1, 1, 14), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("16", HourOfDay.of(2018, 1, 1, 15), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("17", HourOfDay.of(2018, 1, 1, 16), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("18", HourOfDay.of(2018, 1, 1, 17), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("19", HourOfDay.of(2018, 1, 1, 18), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("20", HourOfDay.of(2018, 1, 1, 19), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("21", HourOfDay.of(2018, 1, 1, 20), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("22", HourOfDay.of(2018, 1, 1, 21), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("23", HourOfDay.of(2018, 1, 1, 22), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("24", HourOfDay.of(2018, 1, 1, 23), 1L, 1L, 1L));
        longerThanADay.add(new SiteStatistic("25", HourOfDay.of(2018, 1, 2, 0), 1L, 1L, 1L));

        // When/Then
        assertThatThrownBy(() -> OneDayStatisticsDto
                .from(longerThanADay))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't process more than a day for one day of statistics.");
    }

    @Test
    public void test_that_when_first_hour_doesnt_start_at_midnight_that_exception_is_thrown() {
        // Given
        TreeSet<SiteStatistic> invalid = new TreeSet<>();
        invalid.add(new SiteStatistic("1", HourOfDay.of(2018, 1, 1, 12), 1L, 1L, 1L));
        invalid.add(new SiteStatistic("2", HourOfDay.of(2018, 1, 1, 13), 1L, 1L, 1L));

        // When/Then
        assertThatThrownBy(() -> OneDayStatisticsDto
                .from(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The site statistics for a page should always start at midnight.");
    }

    @Test
    public void test_that_expected_dto_is_returned_for_full_day() {

    }

    @Test
    public void test_that_expected_dto_is_returned_for_partial_day() {

    }
}
