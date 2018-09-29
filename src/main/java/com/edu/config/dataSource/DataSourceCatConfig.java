package com.edu.config.dataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author Tangzhihao
 * @date 2018/3/23
 */
@Configuration
@MapperScan(basePackages = "com.edu.dao.mapper.cat",sqlSessionTemplateRef = "catSqlSessionTemplate")
//给dao层指定mapper注入sqlSessionTemplate
public class DataSourceCatConfig {


    @Bean("catDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.cat")
    public DataSource catDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("catSqlSessionFactory")
    public SqlSessionFactory catSqlSessionFactory(@Qualifier("catDataSource")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean ssfBean = new SqlSessionFactoryBean();
        ssfBean.setDataSource(dataSource);
        ssfBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:com/edu/dao/mapper/cat/*.xml"));
        return ssfBean.getObject();
    }

    @Bean("catTransactionManager")
    public DataSourceTransactionManager catDataSourceTransactionManager(@Qualifier("catDataSource")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("catSqlSessionTemplate")
    public SqlSessionTemplate catSqlSessionTemplate(@Qualifier("catSqlSessionFactory")SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
