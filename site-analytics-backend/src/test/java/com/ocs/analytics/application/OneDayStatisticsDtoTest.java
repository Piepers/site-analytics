package com.ocs.analytics.application;

import com.ocs.analytics.domain.HourOfDay;
import com.ocs.analytics.domain.SiteStatistic;
import com.ocs.analytics.domain.WeatherMeasurement;
import io.vertx.core.json.JsonArray;
import org.junit.jupiter.api.Test;

import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
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
        // Given
        TreeSet<SiteStatistic> aDay = new TreeSet<>();
        aDay.add(new SiteStatistic("1", HourOfDay.of(2018, 1, 1, 0), 1L, 1L, 0L).weatherMeasurement(new WeatherMeasurement(1, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("2", HourOfDay.of(2018, 1, 1, 1), 2L, 11L, 56L).weatherMeasurement(new WeatherMeasurement(2, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("3", HourOfDay.of(2018, 1, 1, 2), 0L, 13L, 15L).weatherMeasurement(new WeatherMeasurement(3, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("4", HourOfDay.of(2018, 1, 1, 3), 50L, 14L, 7L).weatherMeasurement(new WeatherMeasurement(4, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("5", HourOfDay.of(2018, 1, 1, 4), 12L, 11L, 6L).weatherMeasurement(new WeatherMeasurement(5, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("6", HourOfDay.of(2018, 1, 1, 5), 5L, 6L, 2L).weatherMeasurement(new WeatherMeasurement(6, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("7", HourOfDay.of(2018, 1, 1, 6), 3L, 8L, 89L).weatherMeasurement(new WeatherMeasurement(7, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("8", HourOfDay.of(2018, 1, 1, 7), 12L, 5L, 6L).weatherMeasurement(new WeatherMeasurement(8, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("9", HourOfDay.of(2018, 1, 1, 8), 11L, 4L, 7L).weatherMeasurement(new WeatherMeasurement(9, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("10", HourOfDay.of(2018, 1, 1, 9), 89L, 3L, 2L).weatherMeasurement(new WeatherMeasurement(10, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("11", HourOfDay.of(2018, 1, 1, 10), 3L, 0L, 3L).weatherMeasurement(new WeatherMeasurement(11, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("12", HourOfDay.of(2018, 1, 1, 11), 1L, 1L, 4L).weatherMeasurement(new WeatherMeasurement(12, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("13", HourOfDay.of(2018, 1, 1, 12), 55L, 15L, 789L).weatherMeasurement(new WeatherMeasurement(13, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("14", HourOfDay.of(2018, 1, 1, 13), 44L, 13L, 987L).weatherMeasurement(new WeatherMeasurement(14, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("15", HourOfDay.of(2018, 1, 1, 14), 33L, 89L, 654L).weatherMeasurement(new WeatherMeasurement(15, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("16", HourOfDay.of(2018, 1, 1, 15), 22L, 301L, 456L).weatherMeasurement(new WeatherMeasurement(16, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("17", HourOfDay.of(2018, 1, 1, 16), 12L, 4L, 123L).weatherMeasurement(new WeatherMeasurement(17, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("18", HourOfDay.of(2018, 1, 1, 17), 900L, 1L, 321L).weatherMeasurement(new WeatherMeasurement(18, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("19", HourOfDay.of(2018, 1, 1, 18), 777L, 5L, 1L).weatherMeasurement(new WeatherMeasurement(19, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("20", HourOfDay.of(2018, 1, 1, 19), 222L, 6L, 2L).weatherMeasurement(new WeatherMeasurement(18, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("21", HourOfDay.of(2018, 1, 1, 20), 111L, 6L, 3L).weatherMeasurement(new WeatherMeasurement(17, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("22", HourOfDay.of(2018, 1, 1, 21), 333L, 2L, 4L).weatherMeasurement(new WeatherMeasurement(16, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("23", HourOfDay.of(2018, 1, 1, 22), 45L, 98L, 5L).weatherMeasurement(new WeatherMeasurement(15, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("24", HourOfDay.of(2018, 1, 1, 23), 89L, 7L, 1L).weatherMeasurement(new WeatherMeasurement(14, 0, 0, 0, false, false, false, false, false, 0, 0)));

        // When
        OneDayStatisticsDto dto = OneDayStatisticsDto.from(aDay);

        // Then
        JsonArray expectedLabels = new JsonArray()
                .add("01 Jan 2018 00")
                .add("01")
                .add("02")
                .add("03")
                .add("04")
                .add("05")
                .add("06")
                .add("07")
                .add("08")
                .add("09")
                .add("10")
                .add("11")
                .add("12")
                .add("13")
                .add("14")
                .add("15")
                .add("16")
                .add("17")
                .add("18")
                .add("19")
                .add("20")
                .add("21")
                .add("22")
                .add("23");

        JsonArray expectedNewUsersData = new JsonArray()
                .add(1L)
                .add(11L)
                .add(13L)
                .add(14L)
                .add(11L)
                .add(6L)
                .add(8L)
                .add(5L)
                .add(4L)
                .add(3L)
                .add(0L)
                .add(1L)
                .add(15L)
                .add(13L)
                .add(89L)
                .add(301L)
                .add(4L)
                .add(1L)
                .add(5L)
                .add(6L)
                .add(6L)
                .add(2L)
                .add(98L)
                .add(7L);

        JsonArray expectedUsersData = new JsonArray()
                .add(1L)
                .add(2L)
                .add(0L)
                .add(50L)
                .add(12L)
                .add(5L)
                .add(3L)
                .add(12L)
                .add(11L)
                .add(89L)
                .add(3L)
                .add(1L)
                .add(55L)
                .add(44L)
                .add(33L)
                .add(22L)
                .add(12L)
                .add(900L)
                .add(777L)
                .add(222L)
                .add(111L)
                .add(333L)
                .add(45L)
                .add(89L);

        JsonArray expectedSessionsData = new JsonArray()
                .add(0L)
                .add(56L)
                .add(15L)
                .add(7L)
                .add(6L)
                .add(2L)
                .add(89L)
                .add(6L)
                .add(7L)
                .add(2L)
                .add(3L)
                .add(4L)
                .add(789L)
                .add(987L)
                .add(654L)
                .add(456L)
                .add(123L)
                .add(321L)
                .add(1L)
                .add(2L)
                .add(3L)
                .add(4L)
                .add(5L)
                .add(1L);

        JsonArray expectedTemperatures = new JsonArray()
                .add(1)
                .add(2)
                .add(3)
                .add(4)
                .add(5)
                .add(6)
                .add(7)
                .add(8)
                .add(9)
                .add(10)
                .add(11)
                .add(12)
                .add(13)
                .add(14)
                .add(15)
                .add(16)
                .add(17)
                .add(18)
                .add(19)
                .add(18)
                .add(17)
                .add(16)
                .add(15)
                .add(14);

        assertThat(dto.getLabels()).isEqualTo(expectedLabels);
        assertThat(dto.getNewUsersData()).isEqualTo(expectedNewUsersData);
        assertThat(dto.getUsersData()).isEqualTo(expectedUsersData);
        assertThat(dto.getSessionsData()).isEqualTo(expectedSessionsData);
        assertThat(dto.getTempData()).isEqualTo(expectedTemperatures);
    }

    @Test
    public void test_that_expected_dto_is_returned_for_partial_day() {
        // Given
        TreeSet<SiteStatistic> aDay = new TreeSet<>();
        aDay.add(new SiteStatistic("1", HourOfDay.of(2018, 1, 1, 0), 1L, 1L, 0L).weatherMeasurement(new WeatherMeasurement(1, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("2", HourOfDay.of(2018, 1, 1, 1), 2L, 11L, 56L).weatherMeasurement(new WeatherMeasurement(2, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("3", HourOfDay.of(2018, 1, 1, 2), 0L, 13L, 15L).weatherMeasurement(new WeatherMeasurement(3, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("4", HourOfDay.of(2018, 1, 1, 3), 50L, 14L, 7L).weatherMeasurement(new WeatherMeasurement(4, 0, 0, 0, false, false, false, false, false, 0, 0)));
        aDay.add(new SiteStatistic("5", HourOfDay.of(2018, 1, 1, 4), 12L, 11L, 6L).weatherMeasurement(new WeatherMeasurement(5, 0, 0, 0, false, false, false, false, false, 0, 0)));

        // When
        OneDayStatisticsDto dto = OneDayStatisticsDto.from(aDay);

        // Then
        JsonArray expectedLabels = new JsonArray()
                .add("01 Jan 2018 00")
                .add("01")
                .add("02")
                .add("03")
                .add("04");

        JsonArray expectedNewUsersData = new JsonArray()
                .add(1L)
                .add(11L)
                .add(13L)
                .add(14L)
                .add(11L);

        JsonArray expectedUsersData = new JsonArray()
                .add(1L)
                .add(2L)
                .add(0L)
                .add(50L)
                .add(12L);

        JsonArray expectedSessionsData = new JsonArray()
                .add(0L)
                .add(56L)
                .add(15L)
                .add(7L)
                .add(6L);

        JsonArray expectedTemperatures = new JsonArray()
                .add(1)
                .add(2)
                .add(3)
                .add(4)
                .add(5);

        assertThat(dto.getLabels()).isEqualTo(expectedLabels);
        assertThat(dto.getNewUsersData()).isEqualTo(expectedNewUsersData);
        assertThat(dto.getUsersData()).isEqualTo(expectedUsersData);
        assertThat(dto.getSessionsData()).isEqualTo(expectedSessionsData);
        assertThat(dto.getTempData()).isEqualTo(expectedTemperatures);

    }
}
