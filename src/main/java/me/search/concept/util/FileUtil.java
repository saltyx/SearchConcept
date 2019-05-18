package me.search.concept.util;

import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtil {

    public static void writeTokenFile(String token) throws Exception {
        LogUtil.info("写入 " + token);
        LogUtil.info(getConfigPaht());
        Files.write(Paths.get(getConfigPaht()), token.getBytes());
    }

    private static String currentPath() {
        return System.getProperty("user.dir");
    }

    public static String getConfigPaht() {
        return currentPath() + "\\config\\token.token";
    }

    public static String getDbPath() {
        return currentPath() + "\\database\\concepts.db";
    }

}
