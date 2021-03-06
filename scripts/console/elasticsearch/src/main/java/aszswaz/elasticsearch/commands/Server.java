package aszswaz.elasticsearch.commands;

import aszswaz.elasticsearch.connection.Connection;
import aszswaz.elasticsearch.exception.CommandException;
import aszswaz.elasticsearch.mapper.ElasticsearchMapper;
import aszswaz.elasticsearch.po.ConnectionInfo;
import aszswaz.elasticsearch.util.Mybatis;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import lombok.extern.log4j.Log4j2;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * server指令
 *
 * @author aszswaz
 * @date 2021/7/12 18:46:12
 * @IDE IntelliJ IDEA
 */
@Log4j2
@SuppressWarnings({"JavaDoc", "DuplicatedCode"})
public class Server implements Command {
    @Override
    public void execute(String[] args) throws Exception {
        final String commend = args[0];
        if ("insert".equalsIgnoreCase(commend)) {
            this.insert(args);
        } else if ("update".equals(commend)) {
            this.update(args);
        } else if ("delete".equals(commend)) {
            this.delete(args);
        } else if ("select".equals(commend)) {
            this.select();
        } else {
            log.error("未知指令：{}", commend);
            this.help();
        }
    }

    /**
     * 添加连接信息
     */
    private void insert(String[] args) throws CommandException {
        final ConnectionInfo connectionInfo = parseParameter(args);
        if (isBlank(connectionInfo.getUrl())) {
            throw new CommandException("url 不能为空");
        }
        if (isNoneBlank(connectionInfo.getUsername()) && isBlank(connectionInfo.getPassword())) {
            throw new CommandException("密码不能为空");
        }

        ElasticsearchMapper mapper = Mybatis.session().getMapper(ElasticsearchMapper.class);
        // 判断是否存在
        if (nonNull(mapper.selectByUrlUsername(connectionInfo.getUrl(), connectionInfo.getUsername()))) {
            throw new CommandException("服务器： " + connectionInfo.getUrl() + " 用户名： " + connectionInfo.getUsername() + " 已存在");
        }
        // 查询id最大的记录
        final ConnectionInfo maxIdInfo = mapper.selectMaxId();
        if (isNull(maxIdInfo)) {
            connectionInfo.setId(1L);
            connectionInfo.setDefaultAddress(true);
        } else {
            connectionInfo.setId(maxIdInfo.getId() + 1);
        }
        // 设置默认用户
        connectionInfo.setDefaultUser(true);
        // 用户要求当前连接设置为默认，取消其他连接的默认设置
        if (nonNull(connectionInfo.getDefaultAddress()) && connectionInfo.getDefaultAddress()) {
            mapper.cancelAddressDefault();
        }
        if (nonNull(connectionInfo.getDefaultUser()) && connectionInfo.getDefaultUser()) {
            mapper.cancelUserDefault(connectionInfo.getUrl());
        }
        // 添加用户
        mapper.insertServer(connectionInfo);
    }

    /**
     * 解析命令行参数
     */
    private ConnectionInfo parseParameter(String[] args) throws CommandException {
        // 解析命令行参数，提取 url 用户名 密码
        ConnectionInfo connectionInfo = Connection.parseParameter(Arrays.copyOfRange(args, 1, args.length));
        for (int i = 0; i < args.length; i++) {
            if (!args[i].startsWith("-") || "-u".equals(args[i])) continue;
            if ("--default".equals(args[i])) {
                if (i == args.length - 1) throw new CommandException(args[i] + " 参数格式错误");
                // 完全默认
                boolean b = Boolean.parseBoolean(args[++i]);
                connectionInfo.setDefaultAddress(b);
                connectionInfo.setDefaultUser(b);
            } else if ("--default-user".equals(args[i])) {
                // url的默认使用账户
                if (i == args.length - 1) throw new CommandException(args[i] + " 参数格式错误");
                connectionInfo.setDefaultUser(Boolean.parseBoolean(args[++i]));
            } else if ("--default-address".equals(args[i])) {
                if (i == args.length - 1) throw new CommandException(args[i] + " 参数格式错误");
                connectionInfo.setDefaultAddress(Boolean.parseBoolean(args[++i]));
            }
        }
        return connectionInfo;
    }

    /**
     * 更新
     */
    private void update(String[] args) throws CommandException {
        ConnectionInfo connectionInfo = parseParameter(args);

        ElasticsearchMapper mapper = Mybatis.session().getMapper(ElasticsearchMapper.class);
        // 根据名称或id更新参数
        if (nonNull(connectionInfo.getId())) {
            mapper.updateById(connectionInfo);
        } else if (nonNull(connectionInfo.getName())) {
            mapper.updateByName(connectionInfo);
        } else {
            updateOrDeleteError();
        }
    }

    /**
     * L删除记录
     */
    private void delete(String[] args) throws CommandException {
        ConnectionInfo connectionInfo = parseParameter(args);
        ElasticsearchMapper mapper = Mybatis.session().getMapper(ElasticsearchMapper.class);
        if (nonNull(connectionInfo.getId())) {
            mapper.deleteById(connectionInfo.getId());
        } else if (nonNull(connectionInfo.getName())) {
            mapper.deleteByName(connectionInfo.getName());
        } else {
            updateOrDeleteError();
        }
    }

    /**
     * 更新或删除错误
     */
    private void updateOrDeleteError() throws CommandException {
        String message = "id 或者 name 不能都为空," + System.lineSeparator();
        message += "-i --id 指定记录的id," + System.lineSeparator();
        message += "-n --name 指定记录的name," + System.lineSeparator();
        throw new CommandException(message);
    }

    /**
     * 查询所有记录
     */
    private void select() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ElasticsearchMapper mapper = Mybatis.session().getMapper(ElasticsearchMapper.class);
        final List<ConnectionInfo> connectionInfos = mapper.select();
        if (connectionInfos.isEmpty()) return;
        final Field[] fields = ConnectionInfo.class.getDeclaredFields();
        final Object[] titles = new Object[fields.length];
        // 通过反射获取get方法
        int[] columnWidths = new int[fields.length];
        Map<Field, Method> methodMap = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            columnWidths[i] = name.length();
            titles[i] = name;
            // 首字母大写
            name = name.replaceFirst(String.valueOf(name.charAt(0)), String.valueOf(Character.toUpperCase(name.charAt(0))));
            Class<?> type = field.getType();
            String methodName;
            if ("boolean".equalsIgnoreCase(type.getName())) {
                methodName = "is" + name;
            } else {
                methodName = "get" + name;
            }
            methodMap.put(field, ConnectionInfo.class.getMethod(methodName));
        }

        for (int i = 0; i < fields.length; i++) {
            Method getMethod = methodMap.get(fields[i]);
            for (ConnectionInfo connectionInfo : connectionInfos) {
                Object value = getMethod.invoke(connectionInfo);
                if (value instanceof String) {
                    int length = ((String) value).length();
                    if (length > columnWidths[i]) {
                        columnWidths[i] = length;
                    }
                } else if (value instanceof Long) {
                    int length = ((Long) value).toString().length();
                    if (length > columnWidths[i]) {
                        columnWidths[i] = length;
                    }
                } else if (value instanceof Boolean) {
                    int length = ((Boolean) value).toString().length();
                    if (length > columnWidths[i]) {
                        columnWidths[i] = length;
                    }
                }
            }
        }

        final StringBuilder format = new StringBuilder();
        // 分割线
        final StringBuilder dividingLine = new StringBuilder();
        for (int i = 0; i < columnWidths.length; i++) {
            int len = columnWidths[i] + 2;
            for (int j = 0; j < len; j++) {
                dividingLine.append('-');
            }
            dividingLine.append('-');
            format.append("┆ %-").append(columnWidths[i]).append("s ");
            if (i == columnWidths.length - 1) {
                format.append("┆");
            }
        }
        dividingLine.append('-');
        System.out.println(dividingLine);
        System.out.printf(format + "%n", titles);
        System.out.println(dividingLine);
        // 打印表格的主要部分
        for (ConnectionInfo connectionInfo : connectionInfos) {
            Object[] values = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                Method method = methodMap.get(fields[i]);
                Object value = method.invoke(connectionInfo);
                if (value instanceof String) {
                    values[i] = value;
                } else if (value instanceof Long) {
                    values[i] = ((Long) value).toString();
                } else if (value instanceof Boolean) {
                    values[i] = ((Boolean) value).toString();
                }
            }
            System.out.printf(format + "%n", values);
        }
    }

    /**
     * 打印帮助信息
     */
    @Override
    public void help() {
        final String enter = System.lineSeparator();
        final String message = "指令" + enter +
                "insert     添加服务器" + enter +
                "update     更新服务器" + enter +
                "delete     删除服务器" + enter +
                "select     查看服务器" + enter +
                "参数" + enter +
                "-i --id    指定服务器的id，" + enter +
                "-n --name  指定服务器的名字" + enter +
                "-u         指定服务器的账户，语法： username:password" + enter +
                "--default-address 指定服务器为默认使用的服务器，比如：--default-address true" + enter +
                "--default-user    指定该账户是所属服务器的默认账户，比如：--default-user true" + enter +
                "--default  表示设置 --default-address 和 --default-user，比如--default true" + enter +
                "[http|https]://host:port/ 指定服务器的url" + enter +
                "注意事项" + enter +
                "使用 update 和 delete 的时候，-i 和 -n 必须指定其中一个作为服务器唯一标识符，才能正确的执行命令";
        System.err.println(message);
    }
}
