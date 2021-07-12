package aszswaz.elasticsearch.exception;

import java.io.IOException;

/**
 * htt请求异常
 *
 * @author aszswaz
 * @date 2021/7/12 17:29:26
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public class HttpException extends IOException {
    public HttpException(String message) {
        super(message);
    }
}
