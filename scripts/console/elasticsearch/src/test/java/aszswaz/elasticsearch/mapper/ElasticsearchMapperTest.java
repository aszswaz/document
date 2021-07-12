package aszswaz.elasticsearch.mapper;

import aszswaz.elasticsearch.po.ConnectionInfo;
import aszswaz.elasticsearch.util.Mybatis;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author aszswaz
 * @date 2021/7/12 14:52:17
 * @IDE IntelliJ IDEA
 */
@Slf4j
@SuppressWarnings("JavaDoc")
class ElasticsearchMapperTest {
    private ElasticsearchMapper mapper;

    @BeforeEach
    void setUp() {
        SqlSession session = Mybatis.session();
        this.mapper = session.getMapper(ElasticsearchMapper.class);
    }

    @Test
    void createTable() {
        this.mapper.createTable();
    }

    @Test
    void showTable() {
        for (String s : mapper.showTable()) {
            System.out.println(s);
        }
    }

    @Test
    void selectByUrlDefault() {
        System.out.println(this.mapper.selectByUrlDefault("http://localhost:9200"));
    }

    @Test
    void updateById() {
        ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setId(1L);
        connectionInfo.setDefaultUser(true);
        connectionInfo.setDefaultAddress(true);
        mapper.updateById(connectionInfo);
    }
}