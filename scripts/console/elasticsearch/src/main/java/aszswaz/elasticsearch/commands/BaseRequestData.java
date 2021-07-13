package aszswaz.elasticsearch.commands;

import static aszswaz.elasticsearch.config.SystemConfig.DEFAULT_QUERY_FILE;
import static aszswaz.elasticsearch.container.Container.getParameter;
import aszswaz.elasticsearch.entity.HttpGetWithEntity;
import aszswaz.elasticsearch.exception.CommandException;
import aszswaz.elasticsearch.po.ConnectionInfo;
import aszswaz.elasticsearch.util.HttpUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 请求数据的指令
 *
 * @author aszswaz
 * @date 2021/7/13 11:25:58
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public abstract class BaseRequestData implements Command {
    protected final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * 拼接url
     */
    protected String splicingUrl(ConnectionInfo connectionInfo) throws CommandException {
        if (isBlank(connectionInfo.getUrl())) {
            throw new CommandException("请指定服务器的 url，或者使用 server insert --default true url，添加默认服务器");
        }

        // 读取index和query参数
        String index = getParameter("-i");
        if (isBlank(index)) index = getParameter("--index");
        // 保证index末尾没有 “/”
        if (isNoneBlank(index) && index.endsWith("/")) index = index.substring(0, index.indexOf("/"));
        // 拼接url
        String url = connectionInfo.getUrl();
        if (isNoneBlank(index)) url += index;
        return url;
    }

    /**
     * 获得请求体（查询条件）
     */
    protected String getBody() throws IOException {
        String query = getParameter("-q");
        if (isBlank(query)) query = getParameter("--query");
        File queryFile;
        String jsonBody;
        if (isNoneBlank(query)) {
            // 读取指定文件
            queryFile = new File(query);
            jsonBody = this.readFile(queryFile);
        } else if (DEFAULT_QUERY_FILE.exists()) {
            // 读取当前目录下默认的查询条件文件
            queryFile = DEFAULT_QUERY_FILE;
            jsonBody = this.readFile(queryFile);
        } else {
            // 尝试读取控制台输入流
            jsonBody = this.readStdin();
        }
        if (isBlank(jsonBody)) return null;

        // 检测json格式, 启用所有错误检查，不会因为一个错误而终止检查，并且输出错误发生位置的详细json对象
        this.jsonMapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        // 启用重复的key异常
        this.jsonMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        this.jsonMapper.readTree(jsonBody);
        return jsonBody;
    }

    /**
     * 读取文件
     */
    protected String readFile(File jsonFile) throws IOException {
        RandomAccessFile accessFile = null;
        ByteArrayOutputStream byteStream = null;
        try {
            accessFile = new RandomAccessFile(jsonFile, "r");
            byte[] buff = new byte[1024];
            byteStream = new ByteArrayOutputStream(buff.length);
            int len;
            while ((len = accessFile.read(buff)) != -1) {
                byteStream.write(buff, 0, len);
            }
            return byteStream.toString(StandardCharsets.UTF_8.name());
        } finally {
            if (nonNull(accessFile)) accessFile.close();
            if (nonNull(byteStream)) byteStream.close();
        }
    }

    /**
     * 从控制台输入流读取查询条件的json
     */
    protected String readStdin() throws IOException {
        ByteArrayOutputStream byteStream = null;
        try (InputStream stdin = System.in) {
            byte[] buff = new byte[1024];
            byteStream = new ByteArrayOutputStream(buff.length);
            int len;
            while (stdin.available() != 0 && (len = stdin.read(buff)) != -1) {
                byteStream.write(buff, 0, len);
            }
            if (byteStream.size() == 0) return null;
            return byteStream.toString(StandardCharsets.UTF_8.name());
        } finally {
            if (nonNull(byteStream)) byteStream.close();
        }
    }

    /**
     * 请求数据
     */
    protected void requestData(String url, String body, ConnectionInfo connectionInfo) throws IOException {
        final HttpClient httpClient = HttpUtil.getClient(connectionInfo);
        HttpGetWithEntity get = new HttpGetWithEntity(url);
        if (isNoneBlank(body)) {
            StringEntity stringEntity = new StringEntity(body, ContentType.APPLICATION_JSON);
            get.setEntity(stringEntity);
        }
        HttpResponse response = httpClient.execute(get);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}
