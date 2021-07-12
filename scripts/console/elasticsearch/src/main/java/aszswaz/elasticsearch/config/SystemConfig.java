package aszswaz.elasticsearch.config;

import org.apache.commons.lang3.SystemUtils;

import java.io.File;

/**
 * 基本配置
 *
 * @author aszswaz
 * @date 2021/7/12 14:10:29
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings({"JavaDoc", "ResultOfMethodCallIgnored"})
public class SystemConfig {
    /**
     * 配置目录
     */
    public static final File CONFIG_DIR = new File(SystemUtils.USER_HOME + "/" + ".config/elasticsearch/");
    /**
     * es默认端口号
     */
    public static final int DEFAULT_PORT = 9200;

    static {
        if (!CONFIG_DIR.exists()) {
            CONFIG_DIR.mkdirs();
        }
    }
}
