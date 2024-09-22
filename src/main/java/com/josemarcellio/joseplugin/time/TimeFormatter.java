package com.josemarcellio.joseplugin.time;

import java.time.Instant;

public class TimeFormatter implements TimeFormatCustomizer {

    private String dayString = "days";
    private String hourString = "hours";
    private String minuteString = "minutes";
    private String secondString = "seconds";

    @Override
    public TimeFormatter setDayString(String dayString) {
        this.dayString = dayString;
        return this;
    }

    @Override
    public TimeFormatter setHourString(String hourString) {
        this.hourString = hourString;
        return this;
    }

    @Override
    public TimeFormatter setMinuteString(String minuteString) {
        this.minuteString = minuteString;
        return this;
    }

    @Override
    public TimeFormatter setSecondString(String secondString) {
        this.secondString = secondString;
        return this;
    }

    public String formattedTime(Instant instant) {
        if (instant == null) {
            return "Time not available";
        }

        Instant now = Instant.now();
        TimeDuration timeDuration = TimeDuration.between(now, instant);

        StringBuilder result = new StringBuilder();

        if (timeDuration.days() > 0) {
            result.append(String.format("%d %s, ", timeDuration.days(), dayString));
        }
        if (timeDuration.hours() > 0 || timeDuration.days() > 0) {
            result.append(String.format("%d %s, ", timeDuration.hours(), hourString));
        }
        if (timeDuration.minutes() > 0 || timeDuration.hours() > 0 || timeDuration.days() > 0) {
            result.append(String.format("%d %s, ", timeDuration.minutes(), minuteString));
        }
        result.append(String.format("%d %s", timeDuration.seconds(), secondString));

        return result.toString();
    }
}