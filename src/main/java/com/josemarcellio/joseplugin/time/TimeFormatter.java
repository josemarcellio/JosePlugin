/*
 * Copyright (C) 2024 Jose Marcellio
 * GitHub: https://github.com/josemarcellio
 *
 * This software is open-source and distributed under the GNU General Public License (GPL), version 3.
 * You are free to modify, share, and distribute it as long as the same freedoms are preserved.
 *
 * No warranties are provided with this software. It is distributed in the hope that it will be useful,
 * but WITHOUT ANY IMPLIED WARRANTIES, including but not limited to the implied warranties of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * For more details, refer to the full license at <https://www.gnu.org/licenses/>.
 */

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