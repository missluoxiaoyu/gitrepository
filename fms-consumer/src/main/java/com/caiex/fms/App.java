package com.caiex.fms;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.caiex.fms.redis.RedisProperties;
import com.caiex.fms.utils.biYingAgentUrlInfo;
import com.caiex.fms.utils.xiaomiAgentUrlInfo;

@EnableAutoConfiguration(exclude= {DataSourceAutoConfiguration.class})
@SpringBootApplication
@ComponentScan
@ImportResource("classpath:/config/applicationContext.xml")
@EnableScheduling
@ServletComponentScan
@EnableConfigurationProperties({RedisProperties.class,biYingAgentUrlInfo.class,xiaomiAgentUrlInfo.class}) //
public class App{

	
    public static void main( String[] args ){  
    	SpringApplication.run(App.class, args);
    }
    
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
           return new EmbeddedServletContainerCustomizer() {
               @Override
               public void customize(ConfigurableEmbeddedServletContainer container) {
                    container.setSessionTimeout(8*60*60*1000);//单位为S
              }
        };
    }
    
}

