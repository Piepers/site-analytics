package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Represents the current weather of a particular city.
 *
 * @author Bas Piepers
 */
@DataObject
public class CurrentWeather {
    private final City city;
    private final String shortDescription;
    private final String description;
    private final Integer minTemp;
    private final Integer maxTemp;
    private final Long visibility;
    private final Integer wind;
    private final Integer degree;
    private final Integer pressure;
    private final Long sunrise;
    private final Long sunset;
    private final Integer humidity;
    private final Integer clouds;

    public CurrentWeather(City city, String shortDescription, String description, Integer minTemp, Integer maxTemp,
                          Long visibility, Integer wind, Integer degree, Integer pressure, Long sunrise, Long sunset,
                          Integer humidity, Integer clouds) {
        this.city = city;
        this.shortDescription = shortDescription;
        this.description = description;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.visibility = visibility;
        this.wind = wind;
        this.degree = degree;
        this.pressure = pressure;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.humidity = humidity;
        this.clouds = clouds;
    }

    public CurrentWeather(JsonObject jsonObject) {
        this.city = new City(jsonObject.getJsonObject("city"));
        this.shortDescription = jsonObject.getString("shortDescription");
        this.description = jsonObject.getString("description");
        this.minTemp = jsonObject.getInteger("minTemp");
        this.maxTemp = jsonObject.getInteger("maxTemp");
        this.visibility = jsonObject.getLong("visibility");
        this.wind = jsonObject.getInteger("wind");
        this.degree = jsonObject.getInteger("degree");
        this.pressure = jsonObject.getInteger("pressure");
        this.sunrise = jsonObject.getLong("sunrise");
        this.sunset = jsonObject.getLong("sunset");
        this.humidity = jsonObject.getInteger("humidity");
        this.clouds = jsonObject.getInteger("clouds");
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public Integer getMinTemp() {
        return minTemp;
    }

    public Integer getMaxTemp() {
        return maxTemp;
    }

    public Long getVisibility() {
        return visibility;
    }

    public Integer getWind() {
        return wind;
    }

    public Integer getDegree() {
        return degree;
    }

    public Integer getPressure() {
        return pressure;
    }

    public Long getSunrise() {
        return sunrise;
    }

    public Long getSunset() {
        return sunset;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Integer getClouds() {
        return clouds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrentWeather that = (CurrentWeather) o;

        if (!city.equals(that.city)) return false;
        if (!shortDescription.equals(that.shortDescription)) return false;
        if (!description.equals(that.description)) return false;
        if (!minTemp.equals(that.minTemp)) return false;
        if (!maxTemp.equals(that.maxTemp)) return false;
        if (!visibility.equals(that.visibility)) return false;
        if (!wind.equals(that.wind)) return false;
        if (!degree.equals(that.degree)) return false;
        if (!pressure.equals(that.pressure)) return false;
        if (!sunrise.equals(that.sunrise)) return false;
        if (!sunset.equals(that.sunset)) return false;
        if (!humidity.equals(that.humidity)) return false;
        return clouds.equals(that.clouds);
    }

    @Override
    public int hashCode() {
        int result = city.hashCode();
        result = 31 * result + shortDescription.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + minTemp.hashCode();
        result = 31 * result + maxTemp.hashCode();
        result = 31 * result + visibility.hashCode();
        result = 31 * result + wind.hashCode();
        result = 31 * result + degree.hashCode();
        result = 31 * result + pressure.hashCode();
        result = 31 * result + sunrise.hashCode();
        result = 31 * result + sunset.hashCode();
        result = 31 * result + humidity.hashCode();
        result = 31 * result + clouds.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "city=" + city +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", minTemp=" + minTemp +
                ", maxTemp=" + maxTemp +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", degree=" + degree +
                ", pressure=" + pressure +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                ", humidity=" + humidity +
                ", clouds=" + clouds +
                '}';
    }
}
