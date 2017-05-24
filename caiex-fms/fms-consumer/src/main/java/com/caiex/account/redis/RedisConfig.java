package com.caiex.account.redis;

import java.util.HashSet;
import java.util.Set;

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
	
	private String masterName = "caiexmaster";
	private String password = "1z34S678";
	private String [] sentines = {"192.168.1.5:26479"};//,"192.168.1.5:26579","192.168.1.5:26679"
	
	

	   @Bean  
	    public RedisConnectionFactory redisConnectionFactory(){  
		   Set<String> sentinelHostAndPorts = new HashSet<>();
		   for (int i = 0; i < sentines.length; i++) {
			   sentinelHostAndPorts.add(sentines[i]);  
		  }
	 
	        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration(masterName, sentinelHostAndPorts);
	        JedisPoolConfig poolConfig = new JedisPoolConfig();
	        
	        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(sentinelConfig, poolConfig);
	        redisConnectionFactory.setPassword(password);
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
	        return new StringRedisTemplate(connectionFactory);  
	    }  
	
}
