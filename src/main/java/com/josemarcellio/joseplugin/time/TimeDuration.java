package com.josemarcellio.joseplugin.time;

import java.time.Duration;
import java.time.Instant;

public record TimeDuration(long days, long hours, long minutes, long seconds) {

    public static TimeDuration between(Instant start, Instant end) {
        Duration duration = Duration.between(start, end);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return new TimeDuration(days, hours, minutes, seconds);
    }
}