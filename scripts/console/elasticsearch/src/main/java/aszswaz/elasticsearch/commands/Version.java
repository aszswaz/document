package aszswaz.elasticsearch.commands;

import aszswaz.elasticsearch.connection.Connection;
import aszswaz.elasticsearch.exception.CommandException;
import aszswaz.elasticsearch.exception.HttpException;
import aszswaz.elasticsearch.po.ConnectionInfo;
import aszswaz.elasticsearch.util.HttpUtil;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * 查询es服务器的版本
 *
 * @author aszswaz
 * @date 2021/7/12 13:24:10
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public class Version implements Command {

    @Override
    public void execute(@NotNull String[] args) throws CommandException, IOException {
        // 解析连接信息
        ConnectionInfo connectionInfo = Connection.getConnectionInfo(args);
        getResponse(connectionInfo);
    }

    /**
     * 发送请求获取响应
     */
    private void getResponse(ConnectionInfo elasticsearchInfo) throws IOException {
        HttpGet get = new HttpGet(elasticsearchInfo.getUrl());
        HttpClient client;
        if (isNoneBlank(elasticsearchInfo.getUsername()) && isNoneBlank(elasticsearchInfo.getPassword())) {
            client = HttpUtil.getClient(elasticsearchInfo.getUsername(), elasticsearchInfo.getPassword());
        } else {
            client = HttpUtil.getClient();
        }
        HttpResponse response = client.execute(get);
        StatusLine statusLine = response.getStatusLine();
        String responseString = EntityUtils.toString(response.getEntity());
        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
            throw new HttpException("http请求失败，" + System.lineSeparator() + statusLine + System.lineSeparator() + responseString);
        }
        System.out.println(responseString);
    }
}
