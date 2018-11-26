package com.ocs.analytics.domain;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SiteStatisticTest {

    @Test
    public void test_that_when_statistics_record_is_passed_that_this_is_mapped_as_expected() {
        String record = "2018100213,123,321,333,0";

        SiteStatistic s = SiteStatistic.from(record);
        assertThat(s.year()).isEqualTo(2018);
        assertThat(s.month()).isEqualTo(10);
        assertThat(s.day()).isEqualTo(2);
        assertThat(s.hour()).isEqualTo(13);
        assertThat(s.getUsers()).isEqualTo(123L);
        assertThat(s.getNewUsers()).isEqualTo(321L);
        assertThat(s.getSessions()).isEqualTo(333L);

    }

    @Test
    public void test_that_when_invalid_contents_are_present_that_this_throws_exception() {
        String record = "notvalid";
        assertThatThrownBy(() -> SiteStatistic.from(record)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void test_that_when_invalid_year_is_passed_that_this_throws_exception() {
        String record = "-1111101,123,321,333,0";
        assertThatThrownBy(() -> SiteStatistic.from(record)).isInstanceOf(DateTimeParseException.class);
    }

    @Test
    public void test_that_when_invalid_month_is_passed_that_this_throws_exception() {
        String record = "2018131101,123,321,333,0";
        assertThatThrownBy(() -> SiteStatistic.from(record)).isInstanceOf(DateTimeParseException.class);
    }

    @Test // note that no real gregorian calendar check is done yet.
    public void test_that_when_invalid_day_of_month_is_passed_that_this_throws_exception() {
        String record = "2018113201,123,321,333,0";
        assertThatThrownBy(() -> SiteStatistic.from(record)).isInstanceOf(DateTimeParseException.class);
    }

    @Test
    public void test_that_when_invalid_hour_is_passed_that_this_throws_exception() {
        String record = "2018113025,123,321,333,0";
        assertThatThrownBy(() -> SiteStatistic.from(record)).isInstanceOf(DateTimeParseException.class);
    }
}
