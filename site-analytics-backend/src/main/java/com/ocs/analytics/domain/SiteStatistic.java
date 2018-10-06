package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Represents one statistic record of an imported file of a website. Is capable of mapping from CSV to an instance
 * of itself.
 *
 * @author Bas Piepers
 */
@DataObject
public class SiteStatistic {
    private static final String EXPECTED_FORMAT = "yyyyMMddHH";
    private final String id;
    private final String hourOfDay;
    private final Long users;
    private final Long newUsers;
    private final Long sessions;

    private SiteStatistic(String id, String hourOfDay, Long users, Long newUsers, Long sessions) {
        this.id = id;
        this.hourOfDay = hourOfDay;
        this.users = users;
        this.newUsers = newUsers;
        this.sessions = sessions;
    }

    public SiteStatistic(JsonObject jsonObject) {
        this.id = jsonObject.getString("id");
        this.hourOfDay = jsonObject.getString("hourOfDay");
        this.users = jsonObject.getLong("users");
        this.newUsers = jsonObject.getLong("newUsers");
        this.sessions = jsonObject.getLong("sessions");
    }

    public static SiteStatistic from(String csv) {
        // TODO: implement
        return null;
    }

    public String getId() {
        return id;
    }

    public String getHourOfDay() {
        return hourOfDay;
    }

    public Long getUsers() {
        return users;
    }

    public Long getNewUsers() {
        return newUsers;
    }

    public Long getSessions() {
        return sessions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteStatistic that = (SiteStatistic) o;

        if (!id.equals(that.id)) return false;
        if (!hourOfDay.equals(that.hourOfDay)) return false;
        if (!users.equals(that.users)) return false;
        if (!newUsers.equals(that.newUsers)) return false;
        return sessions.equals(that.sessions);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + hourOfDay.hashCode();
        result = 31 * result + users.hashCode();
        result = 31 * result + newUsers.hashCode();
        result = 31 * result + sessions.hashCode();
        return result;
    }
}
