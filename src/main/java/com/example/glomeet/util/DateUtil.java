package com.example.glomeet.util;

import java.time.Instant;
import java.util.Date;

public class DateUtil {
    public static Date parseDate(String dateString) {
        Instant instant = Instant.parse(dateString);
        Date date = Date.from(instant);
        return date;
    }
}
