package com.ocs.analytics.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SiteStatisticsTest {
    private SiteStatistics instanceUnderTest;

    @BeforeEach
    public void init() {
        instanceUnderTest = new SiteStatistics();
    }

    @Test
    public void test_that_when_site_statistics_are_empty_that_period_validation_returns_false() {

        // When
        boolean result = instanceUnderTest.spansMoreThanAYear();

        // Then
        assertThat(result).isFalse();

    }

    @Test
    public void test_that_when_site_statistics_span_less_than_a_year_that_period_validation_returns_false() {
        // Given
        instanceUnderTest.addStatistic(new SiteStatistic("1", HourOfDay.of(2018, 1, 1, 0), 1L, 1L, 1L));
        instanceUnderTest.addStatistic(new SiteStatistic("2", HourOfDay.of(2018, 1, 1, 2), 5L, 1L, 10L));
        instanceUnderTest.addStatistic(new SiteStatistic("3", HourOfDay.of(2018, 1, 3, 0), 10L, 9L, 100L));
        instanceUnderTest.addStatistic(new SiteStatistic("4", HourOfDay.of(2018, 1, 4, 0), 22L, 5L, 999L));
        instanceUnderTest.addStatistic(new SiteStatistic("5", HourOfDay.of(2018, 2, 28, 22), 100L, 90L, 89L));
        instanceUnderTest.addStatistic(new SiteStatistic("6", HourOfDay.of(2018, 10, 6, 15), 8L, 1L, 19L));
        instanceUnderTest.addStatistic(new SiteStatistic("7", HourOfDay.of(2018, 11, 2, 17), 9L, 1L, 8L));

        // When
        boolean result = instanceUnderTest.spansMoreThanAYear();

        // Then
        assertThat(result).isFalse();

    }

    @Test
    public void test_that_when_site_statistics_span_a_year_that_period_validation_returns_false() {
        // Given
        instanceUnderTest.addStatistic(new SiteStatistic("1", HourOfDay.of(2018, 1, 1, 0), 1L, 1L, 1L));
        instanceUnderTest.addStatistic(new SiteStatistic("2", HourOfDay.of(2019, 1, 1, 0), 1L, 1L, 1L));

        // When
        boolean result = instanceUnderTest.spansMoreThanAYear();

        // Then
        assertThat(result).isFalse();
    }

    @Test
    public void test_that_when_site_statistics_span_more_than_a_year_but_only_by_hour_that_validation_returns_false() {
        // Given
        instanceUnderTest.addStatistic(new SiteStatistic("1", HourOfDay.of(2018, 1, 1, 0), 1L, 1L, 1L));
        instanceUnderTest.addStatistic(new SiteStatistic("2", HourOfDay.of(2019, 1, 1, 1), 1L, 1L, 1L));

        // When
        boolean result = instanceUnderTest.spansMoreThanAYear();

        // Then
        assertThat(result).isFalse();
    }

    @Test
    public void test_that_when_site_statistics_span_more_than_a_year_in_days_that_validation_returns_true() {
        // Given
        instanceUnderTest.addStatistic(new SiteStatistic("1", HourOfDay.of(2018, 1, 1, 0), 1L, 1L, 1L));
        instanceUnderTest.addStatistic(new SiteStatistic("2", HourOfDay.of(2019, 1, 2, 1), 1L, 1L, 1L));

        // When
        boolean result = instanceUnderTest.spansMoreThanAYear();

        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void test_that_when_site_statistics_span_more_than_a_year_in_months_that_validation_returns_true() {
        // Given
        instanceUnderTest.addStatistic(new SiteStatistic("1", HourOfDay.of(2018, 1, 1, 0), 1L, 1L, 1L));
        instanceUnderTest.addStatistic(new SiteStatistic("2", HourOfDay.of(2019, 2, 2, 1), 1L, 1L, 1L));

        // When
        boolean result = instanceUnderTest.spansMoreThanAYear();

        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void test_that_when_site_statistics_span_more_than_a_year_in_years_that_validation_returns_true() {
        instanceUnderTest.addStatistic(new SiteStatistic("1", HourOfDay.of(2018, 1, 1, 0), 1L, 1L, 1L));
        instanceUnderTest.addStatistic(new SiteStatistic("2", HourOfDay.of(2020, 2, 2, 1), 1L, 1L, 1L));

        // When
        boolean result = instanceUnderTest.spansMoreThanAYear();

        // Then
        assertThat(result).isTrue();
    }

}
