package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Clouds contain items that describes the type of clouds in a weather forecast.
 *
 * @author Bas Piepers
 */
@DataObject
public class Clouds {
    private final Integer all;

    public Clouds(Integer all) {
        this.all = all;
    }

    public Clouds(JsonObject jsonObject) {
        this.all = jsonObject.getInteger("all");
    }
}
