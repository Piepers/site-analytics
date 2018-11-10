package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents some simple fields of a weather station from which we obtained historical data and {@link Measurement}
 * records with input parameters (that describe where the measurement is taken from).
 *
 * @author Bas Piepers
 */
@DataObject
public class WeatherStation {
    private final String location;
    private final HistoricalParameters parameters;
    private final List<Measurement> measurements;

    public WeatherStation(JsonObject jsonObject) {
        this.location = jsonObject.getString("location");
        this.parameters = new HistoricalParameters(jsonObject.getJsonObject("parameters"));
        this.measurements = Objects.nonNull(jsonObject.getJsonArray("measurements")) ? jsonObject
                .getJsonArray("measurements").stream().map(measurement ->
                        new Measurement((JsonObject) measurement)).collect(Collectors.toList()) : new ArrayList<>();
    }

    public String getLocation() {
        return location;
    }

    public HistoricalParameters getParameters() {
        return parameters;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    @Override
    public String toString() {
        return "WeatherStation{" +
                "location='" + location + '\'' +
                ", parameters=" + parameters +
                ", measurements=" + measurements +
                '}';
    }
}
