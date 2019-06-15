package com.ocs.analytics.application;

import com.ocs.analytics.domain.HourOfDay;
import com.ocs.analytics.domain.SiteStatistic;
import com.ocs.analytics.domain.WeatherMeasurement;
import io.vertx.core.json.JsonArray;
import org.junit.jupiter.api.Test;

import javax.swing.event.TreeSelectionEvent;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class SiteStatisticsDtoTest {
    @Test
    public void test_that_when_statistics_are_smaller_than_page_that_first_returns_everything() {
        // Given
        TreeSet<SiteStatistic> siteStatistics = this.generateStatistics(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 2));
        SiteStatisticsDto dto = SiteStatisticsDto.fromOrderedStatistics(siteStatistics);

        // When
        List<OneDayStatisticsDto> page = dto.first();

        // Then
        this.assertFirstLastPage(page);
    }


    @Test
    public void test_that_when_statistics_are_smaller_than_page_that_last_returns_everything() {
        // Given
        TreeSet<SiteStatistic> siteStatistics = this.generateStatistics(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 2));
        SiteStatisticsDto dto = SiteStatisticsDto.fromOrderedStatistics(siteStatistics);

        // When
        List<OneDayStatisticsDto> page = dto.last();

        // Then
        this.assertFirstLastPage(page);
    }

    @Test
    public void test_that_when_statistics_are_larger_than_a_page_that_expected_first_records_are_returned_on_first() {
        TreeSet<SiteStatistic> siteStatistics = this.generateStatistics(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 10));
        SiteStatisticsDto dto = SiteStatisticsDto.fromOrderedStatistics(siteStatistics);

        // When
        List<OneDayStatisticsDto> page = dto.first();

        // Then
        assertThat(page).isNotNull();
        assertThat(page.size()).isEqualTo(3);
        assertThat(page.get(0).getLabels()).isEqualTo(new JsonArray(Arrays.asList("01 Jan 2018 00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")));
        assertThat(page.get(0).getTempData()).isEqualTo(new JsonArray(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24)));
        assertThat(page.get(0).getUsersData()).isEqualTo(new JsonArray(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)));
        assertThat(page.get(0).getNewUsersData()).isEqualTo(new JsonArray(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33)));
        assertThat(page.get(0).getSessionsData()).isEqualTo(new JsonArray(Arrays.asList(20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43)));

        assertThat(page.get(1).getLabels()).isEqualTo(new JsonArray(Arrays.asList("02 Jan 2018 00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")));
        assertThat(page.get(1).getTempData()).isEqualTo(new JsonArray(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24)));
        assertThat(page.get(1).getUsersData()).isEqualTo(new JsonArray(Arrays.asList(24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47)));
        assertThat(page.get(1).getNewUsersData()).isEqualTo(new JsonArray(Arrays.asList(34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57)));
        assertThat(page.get(1).getSessionsData()).isEqualTo(new JsonArray(Arrays.asList(44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67)));

        assertThat(page.get(2).getLabels()).isEqualTo(new JsonArray(Arrays.asList("03 Jan 2018 00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")));
        assertThat(page.get(2).getTempData()).isEqualTo(new JsonArray(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24)));
        assertThat(page.get(2).getUsersData()).isEqualTo(new JsonArray(Arrays.asList(48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71)));
        assertThat(page.get(2).getNewUsersData()).isEqualTo(new JsonArray(Arrays.asList(58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81)));
        assertThat(page.get(2).getSessionsData()).isEqualTo(new JsonArray(Arrays.asList(68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91)));
    }

    @Test
    public void test_that_when_statistics_are_larger_than_a_page_that_expected_last_records_are_returned_on_last() {
        TreeSet<SiteStatistic> siteStatistics = this.generateStatistics(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 10));
        SiteStatisticsDto dto = SiteStatisticsDto.fromOrderedStatistics(siteStatistics);

        // When
        List<OneDayStatisticsDto> page = dto.last();

        // Then
        assertThat(page).isNotNull();
        assertThat(page.size()).isEqualTo(3);
        assertThat(page.get(0).getLabels()).isEqualTo(new JsonArray(Arrays.asList("08 Jan 2018 00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")));
        assertThat(page.get(0).getTempData()).isEqualTo(new JsonArray(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24)));
        assertThat(page.get(0).getUsersData()).isEqualTo(new JsonArray(Arrays.asList(168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191)));
        assertThat(page.get(0).getNewUsersData()).isEqualTo(new JsonArray(Arrays.asList(178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201)));
        assertThat(page.get(0).getSessionsData()).isEqualTo(new JsonArray(Arrays.asList(188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211)));

        assertThat(page.get(1).getLabels()).isEqualTo(new JsonArray(Arrays.asList("09 Jan 2018 00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")));
        assertThat(page.get(1).getTempData()).isEqualTo(new JsonArray(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24)));
        assertThat(page.get(1).getUsersData()).isEqualTo(new JsonArray(Arrays.asList(192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215)));
        assertThat(page.get(1).getNewUsersData()).isEqualTo(new JsonArray(Arrays.asList(202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225)));
        assertThat(page.get(1).getSessionsData()).isEqualTo(new JsonArray(Arrays.asList(212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235)));

        assertThat(page.get(2).getLabels()).isEqualTo(new JsonArray(Arrays.asList("10 Jan 2018 00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")));
        assertThat(page.get(2).getTempData()).isEqualTo(new JsonArray(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24)));
        assertThat(page.get(2).getUsersData()).isEqualTo(new JsonArray(Arrays.asList(216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239)));
        assertThat(page.get(2).getNewUsersData()).isEqualTo(new JsonArray(Arrays.asList(226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249)));
        assertThat(page.get(2).getSessionsData()).isEqualTo(new JsonArray(Arrays.asList(236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259)));

    }

//    @Test
//    public void test_that_next_behavior_yields_expected_behavior() {
//        TreeSet<SiteStatistic> siteStatistics = this.generateStatistics(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 10));
//        SiteStatisticsDto dto = SiteStatisticsDto.fromOrderedStatistics(siteStatistics);
//
//        // When
//        List<OneDayStatisticsDto> page = dto.next();
//
//        // Then
//        page.stream().forEach(d -> System.out.println(d.toJson().encodePrettily()));
//        assertThat(page.size()).isEqualTo(3);
//
//    }
    private TreeSet<SiteStatistic> generateStatistics(LocalDate start, LocalDate end) {
        TreeSet<SiteStatistic> siteStatistics = new TreeSet<>();
        Long userCount = 0L;
        Long newUserCount = 10L;
        Long sessionCount = 20L;

        while (!start.isAfter(end)) {
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 0), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(1)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 1), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(2)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 2), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(3)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 3), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(4)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 4), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(5)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 5), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(6)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 6), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(7)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 7), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(8)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 8), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(9)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 9), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(10)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 10), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(11)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 11), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(12)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 12), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(13)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 13), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(14)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 14), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(15)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 15), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(16)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 16), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(17)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 17), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(18)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 18), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(19)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 19), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(20)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 20), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(21)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 21), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(22)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 22), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(23)));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 23), userCount++, newUserCount++, sessionCount++).weatherMeasurement(WeatherMeasurement.withTemp(24)));
            start = start.plusDays(1);
        }
        return siteStatistics;
    }

    private void assertFirstLastPage(List<OneDayStatisticsDto> page) {
        assertThat(page).isNotNull();
        assertThat(page.size()).isEqualTo(2);
        assertThat(page.get(0).getLabels()).isEqualTo(new JsonArray(Arrays.asList("01 Jan 2018 00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")));
        assertThat(page.get(0).getTempData()).isEqualTo(new JsonArray(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24)));
        assertThat(page.get(0).getUsersData()).isEqualTo(new JsonArray(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)));
        assertThat(page.get(0).getNewUsersData()).isEqualTo(new JsonArray(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33)));
        assertThat(page.get(0).getSessionsData()).isEqualTo(new JsonArray(Arrays.asList(20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43)));
        assertThat(page.get(1).getLabels()).isEqualTo(new JsonArray(Arrays.asList("02 Jan 2018 00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")));
        assertThat(page.get(1).getTempData()).isEqualTo(new JsonArray(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24)));
        assertThat(page.get(1).getUsersData()).isEqualTo(new JsonArray(Arrays.asList(24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47)));
        assertThat(page.get(1).getNewUsersData()).isEqualTo(new JsonArray(Arrays.asList(34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57)));
        assertThat(page.get(1).getSessionsData()).isEqualTo(new JsonArray(Arrays.asList(44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67)));
    }

}
