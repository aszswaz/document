package aszswaz.elasticsearch.commands;

import aszswaz.elasticsearch.connection.Connection;
import aszswaz.elasticsearch.po.ConnectionInfo;

/**
 * 聚合统计
 *
 * @author aszswaz
 * @date 2021/7/13 11:22:59
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public class Count extends BaseRequestData {
    @Override
    public void execute(String[] args) throws Exception {
        ConnectionInfo connectionInfo = Connection.getConnectionInfo(args);
        String url = super.splicingUrl(connectionInfo) + "/_count";
        String body = super.getBody();
        super.requestData(url, body, connectionInfo);
    }
}
