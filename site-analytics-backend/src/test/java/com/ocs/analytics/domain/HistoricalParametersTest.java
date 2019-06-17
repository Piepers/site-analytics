package com.ocs.analytics.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HistoricalParametersTest {
    @Test
    public void test_that_when_an_instance_is_created_with_invalid_parameters_that_this_results_in_an_exception() {
        // Invalid start year
        assertThatThrownBy(() -> HistoricalParameters.forWeatherMeasurement(1, 1, 1, 1,
                1, 1, 1, 1)).isInstanceOf(IllegalStateException.class);

        // Start after end year
        assertThatThrownBy(() -> HistoricalParameters.forWeatherMeasurement(1981, 1, 1,
                1980, 12, 31, 0, 23));


        // Invalid hours
        assertThatThrownBy(() -> HistoricalParameters.forWeatherMeasurement(1981, 1, 1,
                1981, 12, 31, 25, 26));

        // Invalid months
        assertThatThrownBy(() -> HistoricalParameters.forWeatherMeasurement(1981, 13, 1,
                1981, 12, 31, 0, 23));

        // Start month after end month
        assertThatThrownBy(() -> HistoricalParameters.forWeatherMeasurement(1981, 9, 1,
                1981, 2, 31, 0, 23));
    }

    @Test
    public void test_that_when_instance_is_created_with_valid_parameter_leads_to_expected_result() {
        HistoricalParameters historicalParameters = HistoricalParameters.forWeatherMeasurement( 2018, 8,
                1, 2019, 1, 11, 0, 23);

        assertThat(historicalParameters.getStartYear()).isEqualTo(2018);
        assertThat(historicalParameters.getEndYear()).isEqualTo(2019);
        assertThat(historicalParameters.getStartMonth()).isEqualTo(8);
        assertThat(historicalParameters.getEndMonth()).isEqualTo(1);
        assertThat(historicalParameters.getStartHour()).isEqualTo(1);
        assertThat(historicalParameters.getEndHour()).isEqualTo(24);
    }
}
