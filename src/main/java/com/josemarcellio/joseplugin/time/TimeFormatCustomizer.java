package com.josemarcellio.joseplugin.time;

public interface TimeFormatCustomizer {

    TimeFormatter setDayString(String dayString);

    TimeFormatter setHourString(String hourString);

    TimeFormatter setMinuteString(String minuteString);

    TimeFormatter setSecondString(String secondString);
}