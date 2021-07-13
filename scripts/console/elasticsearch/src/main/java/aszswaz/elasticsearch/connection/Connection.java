package aszswaz.elasticsearch.connection;

import aszswaz.elasticsearch.config.SystemConfig;
import aszswaz.elasticsearch.container.Container;
import aszswaz.elasticsearch.exception.CommandException;
import aszswaz.elasticsearch.mapper.ElasticsearchMapper;
import aszswaz.elasticsearch.po.ConnectionInfo;
import aszswaz.elasticsearch.util.Mybatis;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

/**
 * 获取连接信息
 *
 * @author aszswaz
 * @date 2021/7/12 16:10:04
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings({"JavaDoc"})
public class Connection {
    public static final ElasticsearchMapper ELASTICSEARCH_MAPPER = Mybatis.session().getMapper(ElasticsearchMapper.class);

    /**
     * 解析服务器的连接信息
     */
    public static ConnectionInfo getConnectionInfo(String[] args) throws CommandException {
        final ConnectionInfo parameter = parseParameter(args);
        // 从数据库查询连接信息
        ConnectionInfo connectionInfo = findConnectionInfo(parameter);

        // 优先使用命令行的账户参数
        if (isBlank(parameter.getUrl())) {
            // 用户没有指定url，使用数据库的url
            if (nonNull(connectionInfo) && isNoneBlank(connectionInfo.getUrl())) {
                parameter.setUrl(connectionInfo.getUrl());
                parameter.setUsername(connectionInfo.getUsername());
                parameter.setPassword(connectionInfo.getPassword());
            } else {
                error();
            }
        }
        // 用户没有指定用户名，使用数据库中的用户名
        if (isBlank(parameter.getUsername())) {
            parameter.setUsername(connectionInfo.getUsername());
            parameter.setPassword(connectionInfo.getPassword());
        }
        // 用户没有指定密码，使用数据库中的密码
        if (isBlank(parameter.getPassword())) {
            parameter.setPassword(connectionInfo.getPassword());
        }
        return parameter;
    }

    /**
     * 解析参数
     */
    public static ConnectionInfo parseParameter(String[] args) throws CommandException {
        // 先查找参数中的 URL
        String url = findUrl(args);
        ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setUrl(url);

        String user = Container.PARAMETERS.get("-u");
        if (isNoneBlank(user)) {
            String[] strings = user.split(":");
            if (strings.length == 2) {
                connectionInfo.setUsername(strings[0]);
                connectionInfo.setPassword(strings[1]);
            } else if (strings.length == 1) {
                connectionInfo.setUsername(strings[0]);
            } else {
                throw new CommandException("用户名密码格式错误：" + user);
            }
        }
        String name = Container.getParameter("-n");
        if (isBlank(name)) name = Container.getParameter("--name");
        connectionInfo.setName(name);
        if (isNoneBlank(Container.getParameter("--id"))) {
            connectionInfo.setId(Long.valueOf(Container.getParameter("--id")));
        }
        return connectionInfo;
    }

    /**
     * 从参数中查找url
     */
    @SuppressWarnings("ConstantConditions")
    private static String findUrl(String[] args) throws CommandException {
        // 先查找参数中的 URL
        String url = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                i++;
                continue;
            }
            url = args[i];
            break;
        }

        // 处理不规范的url
        if (isNoneBlank(url)) {
            // 检查url的格式
            if (url.matches("http(s)?://[a-z0-9.]{3,255}(:[\\d]{1,5})?(/)?")) {
                // 如果没有端口号，指定端口号9200，末尾没有“/”添加“/”
                if (url.matches("http(s)?://[a-z0-9.]{3,255}")) {
                    url = url + ":" + SystemConfig.DEFAULT_PORT + "/";
                } else if (!url.endsWith("/")) {
                    url += "/";
                }
            } else {
                throw new CommandException("url格式错误");
            }
        }
        return url;
    }

    /**
     * 从数据库查询连接信息
     */
    private static ConnectionInfo findConnectionInfo(ConnectionInfo parameter) {
        if (isNoneBlank(parameter.getUrl())) {
            // 根据url查询默认用户名
            return ELASTICSEARCH_MAPPER.selectByUrlDefault(parameter.getUrl());
        } else if (isNoneBlank(parameter.getName())) {
            return ELASTICSEARCH_MAPPER.selectByName(parameter.getName());
        } else if (parameter.getId() != null) {
            return ELASTICSEARCH_MAPPER.selectById(parameter.getId());
        } else {
            // 用户没有指定url 和 name,从数据库查询默认的连接信息
            return ELASTICSEARCH_MAPPER.selectDefault();
        }
    }

    /**
     * 抛出异常
     */
    private static void error() throws CommandException {
        String message = "数据库中缺少服务器的默认连接信息，你可以通过参数指定" + System.lineSeparator();
        message += "   指定连接服务的url，语法：(http|https)://host:port/" + System.lineSeparator();
        message += "-u 指定连接服务器的账户，语法：username:password" + System.lineSeparator();
        message += "除此之外还可以使用 server remote insert --default 指令添加连接信息" + System.lineSeparator();
        throw new CommandException(message);
    }

    /**
     * 输出帮助信息
     */
    public static void help() {
        System.err.println("-u          指定用户名，如果不指定从sqlite中读取默认的配置（server insert指令例外）");
        System.err.println("[http|https]://host:port/ 服务器的url, 如果不指定，会尝试从sqlite获取默认的服务器配置");
        System.err.println("--id        指定连接的服务器的id");
        System.err.println("-n  --name  指定连接的服务器的名称");
        System.err.println("注意： --id 和 -n 的参数可以通过 server select 获得，--id 和 -n 都不指定就会使用当前设置的默认服务器");
    }
}
