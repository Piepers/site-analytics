package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an item in the forecast of the weather. Contain data for a three hour period of time.
 *
 * @author Bas Piepers
 *
 */
@DataObject
public class ForecastItem {
    private final Long dt;
    private final String dtText;
    private final Float temp;
    private final Float tempMin;
    private final Float tempMax;
    private final Float pressure;
    private final Float seaLevel;
    private final Float groundLevel;
    private final Integer humidity;
    private final Float tempKf;
    private final List<WeatherItem> weather;
    private final Clouds clouds;
    private final Wind wind;

    public ForecastItem(Long dt, String dtText, Float temp, Float tempMin, Float tempMax, Float pressure,
                        Float seaLevel, Float groundLevel, Integer humidity, Float tempKf, List<WeatherItem> weather,
                        Clouds clouds, Wind wind) {
        this.dt = dt;
        this.dtText = dtText;
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.seaLevel = seaLevel;
        this.groundLevel = groundLevel;
        this.humidity = humidity;
        this.tempKf = tempKf;
        this.weather = weather;
        this.clouds = clouds;
        this.wind = wind;
    }

    public ForecastItem(JsonObject jsonObject) {
        this.dt = jsonObject.getLong("dt");
        this.dtText = jsonObject.getString("dtText");
        this.temp = jsonObject.getFloat("temp");
        this.tempMin = jsonObject.getFloat("tempMin");
        this.tempMax = jsonObject.getFloat("tempMax");
        this.pressure = jsonObject.getFloat("pressure");
        this.seaLevel = jsonObject.getFloat("seaLevel");
        this.groundLevel = jsonObject.getFloat("groundLevel");
        this.humidity = jsonObject.getInteger("humidity");
        this.tempKf = jsonObject.getFloat("tempKf");
        this.weather = jsonObject.getJsonArray("weather").stream().map(o-> new WeatherItem((JsonObject)o)).collect(Collectors.toList());
        this.clouds = new Clouds(jsonObject.getJsonObject("clouds"));
        this.wind = new Wind(jsonObject.getJsonObject("wind"));
    }

    public Long getDt() {
        return dt;
    }

    public String getDtText() {
        return dtText;
    }

    public Float getTemp() {
        return temp;
    }

    public Float getTempMin() {
        return tempMin;
    }

    public Float getTempMax() {
        return tempMax;
    }

    public Float getPressure() {
        return pressure;
    }

    public Float getSeaLevel() {
        return seaLevel;
    }

    public Float getGroundLevel() {
        return groundLevel;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Float getTempKf() {
        return tempKf;
    }

    public List<WeatherItem> getWeather() {
        return weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Wind getWind() {
        return wind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForecastItem that = (ForecastItem) o;

        if (!dt.equals(that.dt)) return false;
        if (!dtText.equals(that.dtText)) return false;
        if (!temp.equals(that.temp)) return false;
        if (!tempMin.equals(that.tempMin)) return false;
        if (!tempMax.equals(that.tempMax)) return false;
        if (!pressure.equals(that.pressure)) return false;
        if (!seaLevel.equals(that.seaLevel)) return false;
        if (!groundLevel.equals(that.groundLevel)) return false;
        if (!humidity.equals(that.humidity)) return false;
        if (!tempKf.equals(that.tempKf)) return false;
        if (!weather.equals(that.weather)) return false;
        if (!clouds.equals(that.clouds)) return false;
        return wind.equals(that.wind);
    }

    @Override
    public int hashCode() {
        int result = dt.hashCode();
        result = 31 * result + dtText.hashCode();
        result = 31 * result + temp.hashCode();
        result = 31 * result + tempMin.hashCode();
        result = 31 * result + tempMax.hashCode();
        result = 31 * result + pressure.hashCode();
        result = 31 * result + seaLevel.hashCode();
        result = 31 * result + groundLevel.hashCode();
        result = 31 * result + humidity.hashCode();
        result = 31 * result + tempKf.hashCode();
        result = 31 * result + weather.hashCode();
        result = 31 * result + clouds.hashCode();
        result = 31 * result + wind.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ForecastItem{" +
                "dt=" + dt +
                ", dtText='" + dtText + '\'' +
                ", temp=" + temp +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                ", pressure=" + pressure +
                ", seaLevel=" + seaLevel +
                ", groundLevel=" + groundLevel +
                ", humidity=" + humidity +
                ", tempKf=" + tempKf +
                ", weather=" + weather +
                ", clouds=" + clouds +
                ", wind=" + wind +
                '}';
    }
}
