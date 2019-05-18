package me.search.concept.util;

import java.time.LocalDateTime;

public final class LogUtil {

    public static void info(String text) {
        System.out.println(String.format("[INFO][%s][%s] %s", Thread.currentThread().getName(),
                LocalDateTime.now() ,text));
    }

    public static void info(Object text) {
        info(String.valueOf(text));
    }
}
