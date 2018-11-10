package com.ocs.analytics.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherMeasurementTest {

    @Test
    public void test_that_a_record_is_mapped_as_expected() {
        // This is how a record looks when it is obtained from the KNMI (including whitespaces)
        String measurementRecord = "  260,20180101,    1,   85,     ,    0,    0,   73,    0,    0";
        WeatherMeasurement measurement = WeatherMeasurement.from(measurementRecord);
        assertThat(measurement).isNotNull();
//        assertThat(measurement.getYear()).isEqualTo(2018);
//        assertThat(measurement.getMonth()).isEqualTo(1);
//        assertThat(measurement.getDay()).isEqualTo(1);
//        assertThat(measurement.getHour()).isEqualTo(1);
        assertThat(measurement.getDurPrec()).isEqualTo(0);
        assertThat(measurement.getHumPerc()).isEqualTo(73);
        assertThat(measurement.getMin()).isNull();
        assertThat(measurement.getRain()).isFalse();
        assertThat(measurement.getSnow()).isFalse();
        assertThat(measurement.getSumPrec()).isEqualTo(0);
        assertThat(measurement.getTemperature()).isEqualTo(85);
    }
}
