package aszswaz.elasticsearch.util;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

/**
 * http工具类
 *
 * @author aszswaz
 * @date 2021/7/12 17:14:25
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public class HttpUtil {
    /**
     * 获取一个需要登录的客户端
     */
    public static HttpClient getClient(String username, String password) {
        HttpClientBuilder clientBuilder = HttpClients.custom();

        RequestConfig.Builder requestBuilder = getRequestBuilder();
        // 启用身份验证
        requestBuilder.setAuthenticationEnabled(true);
        Credentials credentials = new UsernamePasswordCredentials(username, password);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // 设置匹配所有域名和端口
        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
        credentialsProvider.setCredentials(authScope, credentials);

        clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        clientBuilder.setDefaultRequestConfig(requestBuilder.build());
        return clientBuilder.build();
    }

    /**
     * 获取一个允许不需要登录的客户端
     */
    public static HttpClient getClient() {
        HttpClientBuilder clientBuilder = HttpClients.custom();
        RequestConfig.Builder requestBuilder = getRequestBuilder();
        clientBuilder.setDefaultRequestConfig(requestBuilder.build());
        return clientBuilder.build();
    }

    /**
     * 请求配置
     */
    private static RequestConfig.Builder getRequestBuilder() {
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        int timeout = 15 * 1000;
        requestBuilder.setSocketTimeout(timeout);
        requestBuilder.setConnectTimeout(timeout);
        requestBuilder.setConnectionRequestTimeout(timeout);
        return requestBuilder;
    }
}
