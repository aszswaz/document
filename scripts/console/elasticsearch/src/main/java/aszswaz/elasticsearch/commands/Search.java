package aszswaz.elasticsearch.commands;

import aszswaz.elasticsearch.connection.Connection;
import aszswaz.elasticsearch.po.ConnectionInfo;

/**
 * 搜索es
 *
 * @author aszswaz
 * @date 2021/7/13 09:16:50
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public class Search extends BaseRequestData {
    @Override
    public void execute(String[] args) throws Exception {
        // 获取连接信息
        final ConnectionInfo connectionInfo = Connection.getConnectionInfo(args);
        // 拼接url
        final String url = this.splicingUrl(connectionInfo) + "/_search";
        // 获得请求体（查询条件）
        final String body = this.getBody();
        // 发起请求数据，并格式化json后打印到控制台
        super.requestData(url, body, connectionInfo);
    }
}
