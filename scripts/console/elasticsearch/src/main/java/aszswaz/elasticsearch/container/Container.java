package aszswaz.elasticsearch.container;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aszswaz
 * @date 2021/7/12 13:42:55
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public class Container {
    /**
     * 参数
     */
    public static final Map<String, String> PARAMETERS = new HashMap<>();
    /**
     * 命令行参数，全局共享
     */
    @Getter
    @Setter
    private static String[] originArgs;

    /**
     * 获取参数
     */
    public static String getParameter(String key) {
        return PARAMETERS.get(key);
    }
}
