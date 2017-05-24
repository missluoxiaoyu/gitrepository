package com.caiex.account.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@EnableTransactionManagement
public class DataBaseConfig {


  
	
  	@Bean 
     public DataSource dataSource(Environment environment) {
         DruidDataSource datasource = new DruidDataSource();
         datasource.setUrl(environment.getProperty("spring.datasource.url"));
         datasource.setUsername(environment.getProperty("spring.datasource.username"));
         datasource.setPassword(environment.getProperty("spring.datasource.password"));
         datasource.setDriverClassName(environment.getProperty("spring.datasource.driverClassName"));
         return  datasource;
     }
}
