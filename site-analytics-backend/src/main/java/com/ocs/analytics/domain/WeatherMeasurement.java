package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;

/**
 * A measurement record as obtained from weather station historical data that contains information about temperatures,
 * humidity and other weather characteristics on an hourly basis.
 * <p>
 * Is referenced to from {@link SiteStatistic} with which this data is enriched.
 * <p>
 * Is capable of mapping from an incoming record to an object of this instance.
 *
 * @author Bas Piepers
 */
@DataObject
public class WeatherMeasurement implements JsonDomainObject, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherMeasurement.class);
    // The pattern we assume the weathermeasurement contains. 15 columns of numerical data where the second  column contains a date pattern.
    public static final String POSNEG_NR_PATTERN = "(-?[1-9]\\d*|0|\\s?)(,|$)";
    public static final String RECORD_PATTERN = "^[0-9]{3},[0-9]{8},(" + POSNEG_NR_PATTERN + "){13}";
    private final Integer temperature; // in 0.1 degrees celsius
    private final Integer durPrec; // duration of the precipitation. Can be 0 for no rainfall. in 0.1 hours.
    private final Integer sumPrec; // hourly sum of precipitation or -1 in case it was less than 0.05mm.
    private final Integer humPerc; // humidity in percentage
    private final Boolean rain; // did it rain in the previous hour?
    private final Boolean snow; // did it snow in the previous hour?
    private final Boolean fog; // did we have fog
    private final Boolean thunder; // did we have thunder
    private final Boolean ice; // did we have ice
    private final Integer sun; // sun hours in 0.1 hours. Can be -1 for <0.05 hours
    private final Integer clouds; // the amount of clouds in 1/8th and 9 in case sky was not visible

    public WeatherMeasurement(JsonObject jsonObject) {
        this.temperature = jsonObject.getInteger("temperature");
        this.durPrec = jsonObject.getInteger("durPrec");
        this.sumPrec = jsonObject.getInteger("sumPrec");
        this.humPerc = jsonObject.getInteger("humPerc");
        this.rain = jsonObject.getBoolean("rain");
        this.snow = jsonObject.getBoolean("snow");
        this.fog = jsonObject.getBoolean("fog");
        this.thunder = jsonObject.getBoolean("thunder");
        this.ice = jsonObject.getBoolean("ics");
        this.sun = jsonObject.getInteger("sun");
        this.clouds = jsonObject.getInteger("clouds");
    }

    /**
     * Maps a record from the weather station data to an instance of this class.
     *
     * @param measurementRecord, a measurement record as obtained from the KNMI website.
     * @return an instance of this class.
     */
    public static WeatherMeasurement from(String[] measurementRecord) {

        if (measurementRecord.length < 15) {
            LOGGER.debug("The record was smaller than expected");
            // We received something we didn't expect. Throw an exception
            throw new IllegalArgumentException("The measurement record did not contain the expected content (" + Arrays.toString(measurementRecord) + ")");
        }

        Integer temperature = Integer.valueOf(measurementRecord[3]);
        Integer sun = Integer.valueOf(measurementRecord[5]);
        Integer durPerc = Integer.valueOf(measurementRecord[6]);
        Integer sumPerc = Integer.valueOf(measurementRecord[7]);
        Integer clouds = Integer.valueOf(measurementRecord[8]);
        Integer humPerc = Integer.valueOf(measurementRecord[9]);
        Boolean fog = Integer.valueOf(measurementRecord[10]) == 0 ? false : true;
        Boolean rain = Integer.valueOf(measurementRecord[11]) == 0 ? false : true;
        Boolean snow = Integer.valueOf(measurementRecord[12]) == 0 ? false : true;
        Boolean thunder = Integer.valueOf(measurementRecord[13]) == 0 ? false : true;
        Boolean ice = Integer.valueOf(measurementRecord[14]) == 0 ? false : true;

        return new WeatherMeasurement(temperature, durPerc, sumPerc, humPerc, rain, snow, fog, thunder, ice, sun, clouds);
    }

    public WeatherMeasurement(Integer temperature, Integer durPrec, Integer sumPrec, Integer humPerc, Boolean rain, Boolean snow, Boolean fog, Boolean thunder, Boolean ice, Integer sun, Integer clouds) {
        this.temperature = temperature;
        this.durPrec = durPrec;
        this.sumPrec = sumPrec;
        this.humPerc = humPerc;
        this.rain = rain;
        this.snow = snow;
        this.fog = fog;
        this.thunder = thunder;
        this.ice = ice;
        this.sun = sun;
        this.clouds = clouds;
    }

    public Integer getTemperature() {
        return temperature;
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

    public Boolean getFog() {
        return fog;
    }

    public Boolean getThunder() {
        return thunder;
    }

    public Boolean getIce() {
        return ice;
    }

    public Integer getSun() {
        return sun;
    }

    public Integer getClouds() {
        return clouds;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "temperature=" + temperature +
                ", durPrec=" + durPrec +
                ", sumPrec=" + sumPrec +
                ", humPerc=" + humPerc +
                ", rain=" + rain +
                ", snow=" + snow +
                '}';
    }
}
