package me.search.concept.util;

import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtil {

    // TODO token放入数据库或者jar内文件
    public static void writeTokenFile(String token) throws Exception {
        LogUtil.info("写入 " + token);
//        Files.write(Paths.get(FileUtil.class.getResource("/config/token.token").toURI()), token.getBytes());
    }

}
