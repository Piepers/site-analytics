package com.ocs.analytics.application;

import com.ocs.analytics.domain.HourOfDay;
import com.ocs.analytics.domain.SiteStatistic;
import org.junit.jupiter.api.Test;

import javax.swing.event.TreeSelectionEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.UUID;

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
        assertThat(page).isNotNull();
        assertThat(page.size()).isEqualTo(2);
        page.stream().forEach(d -> System.out.println(d.toJson().encodePrettily()));
    }


    @Test
    public void test_that_when_statistics_are_smaller_than_page_that_last_returns_everything() {

    }

    @Test
    public void test_that_when_statistics_are_larger_than_a_page_that_expected_first_records_are_returned_on_first() {

    }

    @Test
    public void test_that_when_statistics_are_larger_than_a_page_that_expected_last_records_are_returned_on_last() {

    }

    private TreeSet<SiteStatistic> generateStatistics(LocalDate start, LocalDate end) {
        TreeSet<SiteStatistic> siteStatistics = new TreeSet<>();
        Random random = new Random();
        while (!start.isAfter(end)) {
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 0), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 1), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 2), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 3), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 4), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 5), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 6), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 7), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 8), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 9), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 10), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 11), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 12), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 13), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 14), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 15), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 16), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 17), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 18), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 19), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 20), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 21), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 22), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            siteStatistics.add(new SiteStatistic(UUID.randomUUID().toString(), HourOfDay.of(start.getYear(), start.getMonthValue(), start.getDayOfMonth(), 23), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101)), Long.valueOf(random.nextInt(101))));
            start = start.plusDays(1);
        }
        return siteStatistics;
    }
}
