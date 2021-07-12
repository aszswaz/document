package aszswaz.elasticsearch.mapper;

import aszswaz.elasticsearch.po.ConnectionInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * es mapper
 *
 * @author aszswaz
 * @date 2021/7/12 14:19:01
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public interface ElasticsearchMapper {
    /**
     * 建立表格
     */
    void createTable();

    /**
     * 查询表格
     */
    List<String> showTable();

    /**
     * 查询url对应的默认配置信息
     */
    ConnectionInfo selectByUrlDefault(
            @Param(value = "url") String url
    );

    /**
     * 查询默认的连接信息
     */
    ConnectionInfo selectDefault();

    /**
     * 根据url和用户名，查找连接信息
     */
    ConnectionInfo selectByUrlUsername(
            @Param(value = "url") String url,
            @Param(value = "username") String username
    );

    /**
     * 取消默认使用地址
     */
    void cancelAddressDefault();

    /**
     * 取消，指定 url 的默认用户
     *
     * @param url
     */
    void cancelUserDefault(
            @Param(value = "url") String url
    );

    /**
     * 插入服务器信息
     */
    void insertServer(
            ConnectionInfo connectionInfo
    );

    /**
     * 查询id最大的数据
     */
    ConnectionInfo selectMaxId();

    /**
     * 更新记录
     */
    void updateById(
            ConnectionInfo connectionInfo
    );

    /**
     * 根据名称更新记录
     */
    void updateByName(
            ConnectionInfo connectionInfo
    );

    /**
     * 根据id删除记录
     */
    void deleteById(
            @Param(value = "id") Long id
    );

    /**
     * 更具id 删除记录
     */
    void deleteByName(
            @Param(value = "name") String name
    );

    /**
     * 查询所有记录
     */
    List<ConnectionInfo> select();
}
