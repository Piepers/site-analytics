package com.ocs.analytics.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HistoricalParametersTest {
    @Test
    public void test_that_when_an_instance_is_created_with_invalid_parameters_that_this_results_in_an_exception() {
        // Invalid start year
        assertThatThrownBy(() -> HistoricalParameters.with("nl", 1, 1, 1, 1,
                1, 1, 1, 1, "test")).isInstanceOf(IllegalStateException.class);

        // Start after end year
        assertThatThrownBy(() -> HistoricalParameters.with("be", 1981, 1, 1,
                1980, 12, 31, 0, 23, "test"));


        // Invalid hours
        assertThatThrownBy(() -> HistoricalParameters.with("nl", 1981, 1, 1,
                1981, 12, 31, 25, 26, "test"));

        // Invalid months
        assertThatThrownBy(() -> HistoricalParameters.with("nl", 1981, 13, 1,
                1981, 12, 31, 0, 23, "test"));

        // Start month after end month
        assertThatThrownBy(() -> HistoricalParameters.with("be", 1981, 9, 1,
                1981, 2, 31, 0, 23, "test"));
    }

    @Test
    public void test_that_when_instance_is_created_with_valid_parameter_leads_to_expected_result() {
        HistoricalParameters historicalParameters = HistoricalParameters.with("nl", 2018, 8,
                1, 2019, 1, 11, 0, 23, "test",
                "variable1", "variable2");

        assertThat(historicalParameters.getStartYear()).isEqualTo(2018);
        assertThat(historicalParameters.getEndYear()).isEqualTo(2019);
        assertThat(historicalParameters.getStartMonth()).isEqualTo(8);
        assertThat(historicalParameters.getEndMonth()).isEqualTo(1);
        assertThat(historicalParameters.getStartHour()).isEqualTo(1);
        assertThat(historicalParameters.getEndHour()).isEqualTo(24);
        assertThat(historicalParameters.getStations()).isEqualTo("test");
        assertThat(historicalParameters.getVariables()).containsExactly("variable1", "variable2");

    }
}
