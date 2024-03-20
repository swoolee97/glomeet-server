package com.example.glomeet.util;

import java.time.Instant;
import java.util.Date;

public class DateUtil {
    public static Date parseDate(String dateString) {
        if (dateString == null) {
            return Date.from(Instant.now());
        }
        Instant instant = Instant.parse(dateString);
        Date date = Date.from(instant);
        return date;
    }
}
