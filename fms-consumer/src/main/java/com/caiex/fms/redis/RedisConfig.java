package com.caiex.fms.redis;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
	
	 @Autowired  
	  private RedisProperties redis; 
	
	
	

	   @Bean  
	    public RedisConnectionFactory redisConnectionFactory(){ 
		  System.out.println(redis.toString());
		   
		   Set<String> sentinelHostAndPorts = new HashSet<>();
		   for (int i = 0; i < redis.getSentinels().length; i++) {
			   sentinelHostAndPorts.add(redis.getSentinels()[i]);  
		  }
	 
	        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration(redis.getMasterName(), sentinelHostAndPorts);
	       
	        JedisPoolConfig poolConfig = new JedisPoolConfig();
	        
	        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(sentinelConfig, poolConfig);
	        redisConnectionFactory.setPassword(redis.getPassword());
	        return redisConnectionFactory;  
	    }  
	    @Bean  
	    RedisTemplate template(RedisConnectionFactory connectionFactory) {  
	    	RedisTemplate<Object, Object> template =  new RedisTemplate<>();
	    	template.setConnectionFactory(connectionFactory);
	    	StringRedisSerializer serializer = new StringRedisSerializer();
	    	template.setKeySerializer(serializer);
	    	template.setValueSerializer(serializer);
	    	template.setHashKeySerializer(serializer);
	    	template.setHashValueSerializer(serializer);
	    	template.setDefaultSerializer(serializer);
	       
	    	return template;
	    }  
	
	  /*new StringRedisTemplate(connectionFactory);   */ 
	    
}
