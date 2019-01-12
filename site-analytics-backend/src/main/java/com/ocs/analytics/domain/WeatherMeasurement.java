package com.ocs.analytics.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;

/**
 * A measurement record as obtained from weather station historical data that contains information about temperatures,
 * humidity and other weather characteristics on an hourly basis. Is referenced to from {@link SiteStatistic} with which
 * this data is enriched.
 * <p>
 * Is capable of mapping from an incoming record to an object of this instance.
 *
 * @author Bas Piepers
 */
@DataObject
public class WeatherMeasurement implements JsonDomainObject {
    private final Integer temperature; // in 0.1 degrees celsius
    private Integer min; // minimum temp. in the last 6 hours. Will be null in the rest of the hours.
    private final Integer durPrec; // duration of the precipitation. Can be 0 for no rainfall. in 0.1 hours.
    private final Integer sumPrec; // hourly sum of precipitation or -1 in case it was less than 0.05mm.
    private final Integer humPerc; // humidity in percentage
    private final Boolean rain; // did it rain in the previous hour?
    private final Boolean snow; // did it snow in the previous hour?

    @JsonIgnore
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public WeatherMeasurement(JsonObject jsonObject) {
        this.temperature = jsonObject.getInteger("temperature");
        this.min = jsonObject.getInteger("min");
        this.durPrec = jsonObject.getInteger("durPrec");
        this.sumPrec = jsonObject.getInteger("sumPrec");
        this.humPerc = jsonObject.getInteger("humPerc");
        this.rain = jsonObject.getBoolean("rain");
        this.snow = jsonObject.getBoolean("snow");
    }

    /**
     * Maps a record from the weather station data to an instance of this class.
     *
     * @param measurementRecord, a measurement record as obtained from the KNMI website.
     * @return an instance of this class.
     */
    public static WeatherMeasurement from(String measurementRecord) {
        String[] contents = measurementRecord.replaceAll("\\s+", "").split(",");

        if (contents.length != 10) {
            // We received something we didn't expect. Throw an exception
            throw new IllegalArgumentException("The measurement record did not contain the expected content (" + measurementRecord + ")");
        }

        Integer temperature = Integer.valueOf(contents[3]);
        Integer min = StringUtils.isNotEmpty(contents[4]) ? Integer.valueOf(contents[4]) : null;
        Integer durPerc = Integer.valueOf(contents[5]);
        Integer sumPerc = Integer.valueOf(contents[6]);
        Integer humPerc = Integer.valueOf(contents[7]);
        Boolean rain = Integer.valueOf(contents[8]) == 0 ? false : true;
        Boolean snow = Integer.valueOf(contents[9]) == 0 ? false : true;
        return new WeatherMeasurement(temperature, min, durPerc, sumPerc, humPerc, rain, snow);
    }

    private WeatherMeasurement(Integer temperature, Integer min,
                               Integer durPrec, Integer sumPrec, Integer humPerc, Boolean rain, Boolean snow) {
        this.temperature = temperature;
        this.min = min;
        this.durPrec = durPrec;
        this.sumPrec = sumPrec;
        this.humPerc = humPerc;
        this.rain = rain;
        this.snow = snow;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getDurPrec() {
        return durPrec;
    }

    public Integer getSumPrec() {
        return sumPrec;
    }

    public Integer getHumPerc() {
        return humPerc;
    }

    public Boolean getRain() {
        return rain;
    }

    public Boolean getSnow() {
        return snow;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "temperature=" + temperature +
                ", min=" + min +
                ", durPrec=" + durPrec +
                ", sumPrec=" + sumPrec +
                ", humPerc=" + humPerc +
                ", rain=" + rain +
                ", snow=" + snow +
                '}';
    }
}
