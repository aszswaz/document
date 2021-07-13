package aszswaz.elasticsearch.commands;

import aszswaz.elasticsearch.connection.Connection;
import aszswaz.elasticsearch.po.ConnectionInfo;
import aszswaz.elasticsearch.util.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

/**
 * es的索引操作
 *
 * @author aszswaz
 * @date 2021/7/13 11:48:44
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public class Indices implements Command {
    @Override
    public void execute(String[] args) throws Exception {
        ConnectionInfo connectionInfo = Connection.getConnectionInfo(args);
        String url = connectionInfo.getUrl() + "/_cat/indices?v";
        HttpClient client = HttpUtil.getClient(connectionInfo);
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    /**
     * 输出帮助信息
     */
    @Override
    public void help() {
        Connection.help();
    }
}
