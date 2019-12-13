package com.util;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisUtil {
	private static final Logger logger=Logger.getLogger(RedisUtil.class);
	
	private static JedisPool pool=null;
	private static final String host="127.0.0.1";
	private static final int port=6379;
	private static final int waitTime=1000*100;
	private static final int maxTotal=100;
	private static final int minIdle=5;
	
	/**
	 * 创建连接池
	 * @return
	 */
	public static JedisPool getPool(){
		logger.info(">>>>>>>>>>创建连接池");
		GenericObjectPoolConfig config =new GenericObjectPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMinIdle(minIdle);
		config.setMaxWaitMillis(waitTime);
		config.setTestOnBorrow(true);
        pool = new JedisPool(config, host);
        return pool;
	}
	 /**
     * 同步获取Jedis实例
     * @return Jedis
     */
    public synchronized static Jedis getJedis() {  
        if (pool == null) {  
        	getPool();
        }
        Jedis jedis = null;
        try {  
            if (pool != null) {  
                jedis = pool.getResource(); 
            }
        } catch (Exception e) {  
            logger.error("Get jedis error : "+e);
        }finally{
            returnResource(jedis);
        }
        return jedis;
    }  
	
	
    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null && pool !=null) {
        	pool.returnResourceObject(jedis);
        }
    }
    
    
    /**
     * 设置 String
     * @param key
     * @param value
     */
    public static void setString(String key ,String value){
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            getJedis().set(key,value);
        } catch (Exception e) {
            logger.error("Set key error : "+e);
        }
    }
     
    /**
     * 设置 过期时间
     * @param key
     * @param seconds 以秒为单位
     * @param value
     */
    public static void setString(String key ,int seconds,String value){
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            getJedis().setex(key, seconds, value);
        } catch (Exception e) {
            logger.error("Set keyex error : "+e);
        }
    }
     
    /**
     * 获取String值
     * @param key
     * @return value
     */
    public static String getString(String key){
        if(getJedis() == null || !getJedis().exists(key)){
            return null;
        }
        return getJedis().get(key);
    }
}
