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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Tangzhihao
 * @date 2018/3/23
 */
@Configuration
@MapperScan(basePackages = "com.edu.dao.mapper.ideaDemo",sqlSessionTemplateRef = "ideaDemoSqlSessionTemplate")
//给dao层指定mapper注入sqlSessionTemplate
public class DataSourceIdeaDemoConfig {

    @Bean(name = "ideaDemoDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ideademo")
    @Primary
    public DataSource ideaDemoDataSource(){
        return DataSourceBuilder.create().build();
    }


    @Bean("ideaDemoSqlSessionFactory")
    @Primary
    public SqlSessionFactory ideaDemoSqlSessionFactory(@Qualifier("ideaDemoDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean ssfBean = new SqlSessionFactoryBean();
        ssfBean.setDataSource(dataSource);
        ssfBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:com/edu/dao/mapper/ideaDemo/*.xml"));
        return ssfBean.getObject();
    }

    @Bean("ideaDemoTransactionManager")
    @Primary
    public DataSourceTransactionManager ideaDemoDataSourceTransactionManager(@Qualifier("ideaDemoDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("ideaDemoSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate ideaDemoSqlSessionTemplate(@Qualifier("ideaDemoSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
