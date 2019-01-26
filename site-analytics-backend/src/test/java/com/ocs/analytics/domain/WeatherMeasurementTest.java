package com.ocs.analytics.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherMeasurementTest {

    @Test
    public void test_that_a_record_is_mapped_as_expected() {
        String record = "260,20180101,1,85,,0,0,0,8,73,0,0,0,0,0";

        // This is how a record looks when it is obtained from the KNMI (including whitespaces)
        String[] content = record.split(",");

        WeatherMeasurement measurement = WeatherMeasurement.from(content);
        assertThat(measurement).isNotNull();
        assertThat(measurement.getDurPrec()).isEqualTo(0);
        assertThat(measurement.getHumPerc()).isEqualTo(73);
        assertThat(measurement.getTemperature()).isEqualTo(85);
        assertThat(measurement.getSun()).isEqualTo(0);
        assertThat(measurement.getRain()).isFalse();
        assertThat(measurement.getSnow()).isFalse();
        assertThat(measurement.getSumPrec()).isEqualTo(0);
        assertThat(measurement.getClouds()).isEqualTo(8);
        assertThat(measurement.getFog()).isFalse();
        assertThat(measurement.getThunder()).isFalse();
        assertThat(measurement.getIce()).isFalse();

    }

}
