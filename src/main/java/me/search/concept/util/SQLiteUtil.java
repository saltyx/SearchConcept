package me.search.concept.util;

import java.time.LocalDateTime;

public final class SQLiteUtil {

    public static int today() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear()*10000;
        int month = now.getMonthValue()*100;
        int day = now.getDayOfMonth();
        return year+month+day;
    }

    /**
     * 获取格式为yyyy-MM-dd的日期
     * @return yyyy-MM-dd
     */
    public static String getToday() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        return String.format("%d-%d-%d", year,month, day);
    }
}
