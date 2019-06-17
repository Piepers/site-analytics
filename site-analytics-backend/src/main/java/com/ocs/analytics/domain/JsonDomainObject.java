package com.ocs.analytics.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.json.JsonObject;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface JsonDomainObject {
    default JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }
}
