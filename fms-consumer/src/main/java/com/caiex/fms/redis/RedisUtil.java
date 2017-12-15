package com.caiex.fms.redis;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
	

	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	
	//模糊查询
	public Set<String> getPattern(String key){
		
		return redisTemplate.keys(key+"*");
		
	}
	
	
	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);		
	}
	
	
	public void set(String key, String value, Long expireTime) {
		redisTemplate.opsForValue().set(key, value, expireTime);
	}

	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	
	public void del(String key) {
		redisTemplate.opsForValue().getOperations().delete(key);
	}
	
	
	public void del(Collection<String> keys) {
		redisTemplate.opsForValue().getOperations().delete(keys);
	}

	
	public List<Object> mGet(String[] keys) {
		return redisTemplate.opsForValue().multiGet(Arrays.asList(keys));
		
	}
	
	
	public void mSet(String[] keys, String[] values) {
		Map<String, String> kv = new HashMap<String, String>();
		for (int i = 0; i < keys.length; i++) {
			kv.put(keys[i], values[i]);
		}
		
		redisTemplate.opsForValue().multiSet(kv);
	}

	
	public Double incrByFloat(String key, Double increment) {
		return redisTemplate.opsForValue().increment(key, increment);
	}

	
	public Set<String> keys(String pattern) {
		return redisTemplate.opsForValue().getOperations().keys(pattern);
	}

	
	public String hGet(String key, String field) {
		return (String) redisTemplate.opsForHash().get(key, field);
	}

	
	public void hSet(String key, String field, String value) {
		redisTemplate.opsForHash().put(key, field, value);
	}

	
	public void hDel(String key, String field) {
		redisTemplate.opsForHash().delete(key, field);
	}

	
	public Map<String, String> hGetAll(String key) {
		Map<Object, Object> tmpResult = redisTemplate.opsForHash().entries(key);
		Map<String, String> result = new HashMap<String, String>();
		for (Entry<Object, Object> entry : tmpResult.entrySet()) {
			result.put((String) entry.getKey(), (String) entry.getValue());
		}
		return result;
	}

	
	public Double hIncrByFloat(String key, String field, Double increment) {
		return redisTemplate.opsForHash().increment(key, field, increment);
	}

	
	public Long lPush(String key, String value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	
	public String rPop(String key) {
		return (String) redisTemplate.opsForList().rightPop(key);
	}

	
	public List<Object> lRange(String key, long start, long end) {
		return redisTemplate.opsForList().range(key, start, end);
	}
	
	
	public void mSetWithSameValue(String[] keys, String value) {
		Map<String, String> kv = new HashMap<String, String>();
		for (String key : keys) {
			kv.put(key, value);
		}
		
		redisTemplate.opsForValue().multiSet(kv);
	}
	
	
	public List<Object> lRangeAll(String key) {
		return redisTemplate.opsForList().range(key, 0, -1);
	}
}
