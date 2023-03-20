package com.couchoptions.utils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class AppUtils {
    public static final String START_TIME = "09:00";
    public static final String END_TIME = "15:35";
    public static DateTimeFormatter DD_MMM_YYYY_HH_MM_SS =
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("dd-MMM-yyyy HH:mm:ss")
                    .toFormatter()
                    .withZone(ZoneId.of("Asia/Kolkata"));
    public static DateTimeFormatter DD_MMM_YYYY =
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("dd-MMM-yyyy")
                    .toFormatter()
                    .withZone(ZoneId.of("Asia/Kolkata"));
}
