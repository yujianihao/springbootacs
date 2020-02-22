package com.study.study;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;

import tk.mybatis.spring.annotation.MapperScan;


@Configuration
@MapperScan(basePackages = "com.study.study.mapper", sqlSessionFactoryRef = "sessionFactory")
public class DataSourceConfig {

	@ConfigurationProperties(prefix = "spring.datasource")
	@Bean(name = "dataSource")
	public DruidDataSource druidDataSource(){
	      return new DruidDataSource();
	}
	
	@Bean(name = "sessionFactory")
	public SqlSessionFactory masterSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/com/study/study/mapper/*Mapper.xml"));
		//sessionFactoryBean.setTypeAliasesPackage("com.study.study.mapper");
		return sessionFactoryBean.getObject();
	}
}
