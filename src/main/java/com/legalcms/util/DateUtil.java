package com.legalcms.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Date utility class for formatting dates in ISO-8601 format
 * Frontend expects dates as YYYY-MM-DD
 */
public class DateUtil {

    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(ISO_DATE_FORMATTER);
    }

    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, ISO_DATE_FORMATTER);
    }
}
