package aszswaz.elasticsearch.po;

import lombok.Data;

import java.io.Serializable;

/**
 * es数据库连接信息
 *
 * @author aszswaz
 * @date 2021/7/12 15:17:37
 * @IDE IntelliJ IDEA
 */
@Data
@SuppressWarnings("JavaDoc")
public class ConnectionInfo implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 服务器名称
     */
    private String name;
    /**
     * url资源路径
     */
    private String url;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 默认连接地址
     * 为了让mybatis能够正确的执行 != null 判断，这里需要使用包装类
     */
    private Boolean defaultAddress;
    /**
     * 默认使用账户
     */
    private Boolean defaultUser;
}
