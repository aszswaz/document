package aszswaz.elasticsearch.util;

import static aszswaz.elasticsearch.config.SystemConfig.CONFIG_DIR;
import aszswaz.elasticsearch.mapper.ElasticsearchMapper;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * mybatis数据库框架
 *
 * @author aszswaz
 * @date 2021/7/12 14:03:09
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public class Mybatis {
    private static final SqlSessionFactory SESSION_FACTORY;

    static {
        SqlSessionFactory sqlSessionFactory = null;
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl("jdbc:sqlite:" + new File(CONFIG_DIR, "elasticsearch.db").getAbsolutePath());

            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment = new Environment("development", transactionFactory, dataSource);
            Configuration configuration = new Configuration(environment);
            configuration.addMapper(ElasticsearchMapper.class);
            configuration.setLogImpl(Log4j2Impl.class);

            String resource = "mapper/ElasticsearchMapper.xml";
            InputStream mapperStream = Resources.getResourceAsStream(resource);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperStream, configuration, resource, configuration.getSqlFragments());
            xmlMapperBuilder.parse();

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        SESSION_FACTORY = sqlSessionFactory;
    }

    /**
     * 初始化数据库
     */
    public static void init() {
        SqlSession session = SESSION_FACTORY.openSession();
        ElasticsearchMapper mapper = session.getMapper(ElasticsearchMapper.class);
        List<String> tables = mapper.showTable();
        if (tables.isEmpty() || !tables.contains("elasticsearch_config")) {
            mapper.createTable();
            session.commit();
        }
        session.close();
    }

    /**
     * 开启会话
     */
    public static SqlSession session() {
        return SESSION_FACTORY.openSession(true);
    }
}
