package com.ocs.analytics.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HourOfDayTest {

    @Test
    public void test_that_when_day_and_month_is_lower_than_10_that_0_is_prepended() {
        // Given
        HourOfDay hourOfDay = HourOfDay.of(2019, 1, 1, 0);

        // When
        Integer value = hourOfDay.yearMonthDayAsFormattedInteger();

        // Then
        assertThat(value).isEqualTo(20190101);
    }

    @Test
    public void test_that_when_day_and_month_are_larger_than_10_that_values_are_as_is() {
        // Given
        HourOfDay hourOfDay = HourOfDay.of(2019, 1, 1, 0);

        // When
        Integer value = hourOfDay.yearMonthDayAsFormattedInteger();

        // Then
        assertThat(value).isEqualTo(20190101);
    }
}
