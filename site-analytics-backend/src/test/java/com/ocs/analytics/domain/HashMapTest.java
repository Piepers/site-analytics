package com.ocs.analytics.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class HashMapTest {
    @Test
    public void test_hashmap_speed() {
        Map<Instant, String> instantMap = new HashMap<>();
        Map<Long, String> longMap = new HashMap<>();
        Map<HourOfDay, String> hodMap = new HashMap<>();

        // Put 5000 items in the maps and record the time.
        Instant startTime = Instant.now();
        Instant key = Instant.now();
        for (int i = 0; i < 5000; i++) {
            instantMap.put(key.plus(1, ChronoUnit.MINUTES), "test");
        }
        Instant endTime = Instant.now();
        System.out.println("Putting 5000 items in hashmap key instant as keys took "+ (endTime.toEpochMilli() - startTime.toEpochMilli()) + " ms.");


        startTime = Instant.now();
        key = Instant.now();
        Long lKey = key.toEpochMilli();
        for (int i = 0; i < 5000; i++) {
            longMap.put(key.plus(1, ChronoUnit.MINUTES).toEpochMilli(), "test");
        }
        endTime = Instant.now();
        System.out.println("Putting 5000 items in hashmap key long as keys took "+ (endTime.toEpochMilli() - startTime.toEpochMilli()) + " ms.");

        startTime = Instant.now();
        key = Instant.now();
        LocalDateTime ldt;
        HourOfDay hod;
        for (int i = 0; i < 5000; i++) {
            key = key.plus(1, ChronoUnit.MINUTES);
            ldt = LocalDateTime.ofInstant(key, ZoneId.of("Europe/Amsterdam"));
            hod = HourOfDay.of(ldt.getYear(), ldt.getMonth().getValue(), ldt.getDayOfMonth(), ldt.getHour());
            hodMap.put(hod, "test");
        }
        endTime = Instant.now();
        System.out.println("Putting 5000 items in hashmap key HourOfDay as keys took "+ (endTime.toEpochMilli() - startTime.toEpochMilli()) + " ms.");
    }
}
