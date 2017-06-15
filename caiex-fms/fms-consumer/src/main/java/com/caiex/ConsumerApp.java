package com.caiex;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import com.caiex.account.utils.biYingAgentUrlInfo;
import com.caiex.account.utils.xiaomiAgentUrlInfo;
import com.github.pagehelper.PageHelper;


/**
 * Hello world!
 *
 */
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@MapperScan("com.caiex.account.mapper")
@ImportResource("classpath:/conf/applicationContext.xml")
@EnableScheduling
@ServletComponentScan
@EnableConfigurationProperties({biYingAgentUrlInfo.class,xiaomiAgentUrlInfo.class}) 
@PropertySource(value={"file:C:/fms/config/fms.properties"})
public class ConsumerApp{
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource(){
		
		return new com.caiex.account.config.DataBaseConfig().dataSource(env);
	}
	
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean(PageHelper  pageHelper) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		PathMatchingResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
		
		 // 添加插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[] { pageHelper });
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean
	public PlatformTransactionManager transactionManager(){
		return new DataSourceTransactionManager(dataSource());
	}
	
	 @Bean
	    public PageHelper pageHelper() {
	        PageHelper pageHelper = new PageHelper();
	        Properties p = new Properties();
	        p.setProperty("offsetAsPageNum", "true");
	        p.setProperty("rowBoundsWithCount", "true");
	        p.setProperty("reasonable", "true");
	        p.setProperty("dialect", "mysql");
	        pageHelper.setProperties(p);
	        return pageHelper;
	    }
	
	
    public static void main( String[] args ){  
    	SpringApplication.run(ConsumerApp.class, args);
    	
    }
}
