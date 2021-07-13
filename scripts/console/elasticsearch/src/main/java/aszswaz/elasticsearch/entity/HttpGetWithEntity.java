package aszswaz.elasticsearch.entity;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * 带实体的get请求
 *
 * @author aszswaz
 * @date 2021/7/13 10:41:57
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {
    public HttpGetWithEntity(String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return "GET";
    }
}
