package com.leaderboard.analytics.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Common Date utility.
 *
 * @author Srinivasa Prasad Sunnapu
 */
public class DateUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");

    // hiding utilty constructor
    private DateUtil() { }

    public static List<String> getDatesInRange(String fromDate, String toDate) {
        List<String> dates = new ArrayList<>();
        LocalDate localFromDate = LocalDate.parse(fromDate);
        while(!formatter.format(localFromDate).equals(toDate)) {
            dates.add(formatter.format(localFromDate));
            localFromDate = localFromDate.plusDays(1);
        }
        dates.add(toDate);
        return dates;
    }

    public static String nowDateAsString() {
        return formatter.format(LocalDate.now());
    }

}
