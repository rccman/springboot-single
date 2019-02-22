package com.rcc.test.config.mybatis;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.github.pagehelper.PageHelper;
import com.rcc.test.config.Interceptor.SqlInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by rencc on 2019年2月18日.
 */
@Configuration
@MapperScan(basePackages = "com.rcc.test.dao",sqlSessionTemplateRef = "testSqlSessionTemplate")
public class TestConfig {

    @Value("${spring.datasource.test.driverClassName}")
    private String driverClassName;
    @Value("${spring.datasource.test.url}")
    private String url;
    @Value("${spring.datasource.test.username}")
    private String username;
    @Value("${spring.datasource.test.password}")
    private String password;
    @Value("${spring.datasource.test.typeAliasesPackage}")
    private String typeAliasesPackage;
    @Value("${spring.datasource.test.mybatisConfigLocation}")
    private String mybatisConfigLocation;
    @Value("${sqlLog.isUse}")
    private String sqlLog;

    @Autowired
    private SqlInterceptor sqlLogInter;
    @Autowired
    private PageHelper pageHelper;
    /**
     * 创建数据源
     *
     */
    @Primary
    @Bean(name = "testDataSource")
    public DataSource getDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", driverClassName);
        props.put("url", url);
        props.put("username", username);
        props.put("password", password);
        return DruidDataSourceFactory.createDataSource(props);
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean(name = "testSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("testDataSource")DataSource ds) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        if("true".equals(sqlLog)){
        	fb.setPlugins(new Interceptor[]{pageHelper,sqlLogInter});
        }else{
            fb.setPlugins(new Interceptor[]{pageHelper});
        }
        //指定数据源(这个必须有，否则报错)
        fb.setDataSource(ds);
        //下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        //指定基包
        fb.setTypeAliasesPackage(typeAliasesPackage);
        //指定xml文件位置
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mybatisConfigLocation));

        return fb.getObject();
    }

    @Bean(name = "testTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("testDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "testSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("testSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * PageHelper 分页插件
     * @return
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
