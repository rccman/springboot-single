package com.rcc.test.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类<P>
 * 提供了支持设置有效时间的缓存方法
 * 
 * @author rencc
 * @date 2019年2月20日
 */
@Repository
public class RedisService {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 获取普通存储
	 * @param key
	 * @return
	 */
	public String get(String key){
		return (String) redisTemplate.opsForValue().get(key);
	}

	/**
	 * 获取Hash存储
	 * @param key
	 * @param hashKey
	 * @return
	 */
	public String get(String key, String hashKey){
		return (String) redisTemplate.opsForHash().get(key, hashKey);
	}
	public Map<Object, Object> getAllByKey(String key){
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        return map;
	}

	/**
	 * 获取Hash实体
	 * @param key
	 * @param hashKey
	 * @param valueType
	 * @param <T>
	 * @return
	 */
	public <T> T get(String key, String hashKey,Class<T> valueType) {
		String value = (String) redisTemplate.opsForHash().get(key, hashKey);
		if(value==null){
			return null;
		}
		T t = null;
		try {
			t = objectMapper.readValue(value, valueType);
		} catch (IOException e) {
			log.warn("Json to Bean failure");
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 获取普通实体类
	 * @param key
	 * @param valueType
	 * @param <T>
	 * @return
	 */
	public <T> T get(String key,Class<T> valueType){
		T t = null;
		try {
			t = JsonUtils.jsonToBean(get(key), valueType);
		} catch (Exception e) {
			log.warn("Json to Bean failure");
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 普通存储
	 * @param key
	 * @param value
	 * @param expireTime
	 */
	public void put(String key, Object value, int expireTime) {
		String strValue = getStrValue(value);
		ValueOperations<String, String> ops = redisTemplate.opsForValue();
		ops.set(key,strValue,expireTime, TimeUnit.SECONDS);
	}

	/**
	 * Hash存储
	 * @param key
	 * @param hashKey
	 * @param value
	 * @param timeToLive 有效时间（单位为秒）不设置或设置为0为一直有效
	 *
	 */
	public void put(String key, String hashKey, Object value,  int timeToLive){
		String strValue = getStrValue(value);
		redisTemplate.opsForHash().put(key, hashKey, strValue);
		if(timeToLive!=0){
			redisTemplate.expire(key, timeToLive, TimeUnit.SECONDS);
		}
	}


	/**
	 * 重新给key的数据设置失效时间
	 * @param key
	 * @param hashKey
	 * @param timeToLive
	 */
	public void expire(String key,String hashKey,int timeToLive){
		if(timeToLive != 0){
			redisTemplate.expire(key, timeToLive, TimeUnit.SECONDS);
		}
	}

	/**
	 * 清除redis中的所有Key
	 * @return
	 */
    public boolean  clearRedisCache(){
        Boolean isTrue = new Boolean(true);
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null ) {
             isTrue =false;
        }else {
             for (String key : keys) {
             	redisTemplate.delete(key);
            }
        }
        return isTrue;
    }
    
    /**
     * 清除redis中指定的Key
     * @param key
     * @param hashKeys
     * @return
     */
    public boolean clearRedisCache(String key,List<String> hashKeys) {
        Boolean isTrue = new Boolean(true);
        if (hashKeys == null ) {
            isTrue =false;
        }else {
            for (String hashKey : hashKeys) {
            	redisTemplate.opsForHash().delete(key,hashKey);
            }
        }
        return isTrue;
    }

	/**
	 * @Description: 删除缓存
	 * @param key
	 * @return
	 */
	public void del(String... key){
		if(key!=null && key.length>0){
			if(key.length == 1){  
                redisTemplate.delete(key[0]);  
            }else{  
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
		}
	}

	/**
	 * 获取字符串value
	 * @param value
	 * @return
	 */
	private String getStrValue(Object value) {
		String strValue = "";
		if(value instanceof String){
			strValue = (String) value;
		}else{
			try {
				strValue = objectMapper.writeValueAsString(value);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return strValue;
	}
}
