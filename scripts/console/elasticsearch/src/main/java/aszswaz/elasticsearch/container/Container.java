package aszswaz.elasticsearch.container;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

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
}
