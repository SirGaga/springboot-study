package com.zhangjie.springbootstudy.util;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class JedisUtil {
    @Autowired
    private static JedisPool jedisPool;

    private JedisUtil() {}

    /**
     * 简单的Get
     * @param <T>
     * @param key
     * @param requiredType
     * @return
     */
    public static <T> T get(String key , Class<T>...requiredType){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[] skey = SerializeUtil.serialize(key);
            return SerializeUtil.deserialize(jedis.get(skey),requiredType);
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return null;
    }
    /**
     * 简单的set
     * @param key
     * @param value
     */
    public static void set(Object key ,Object value){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[] skey = SerializeUtil.serialize(key);
            byte[] svalue = SerializeUtil.serialize(value);
            jedis.set(skey, svalue);
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
    }
    /**
     * 过期时间的
     * @param key
     * @param value
     * @param timer （秒）
     */
    public static void setex(Object key, Object value, int timer) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[] skey = SerializeUtil.serialize(key);
            byte[] svalue = SerializeUtil.serialize(value);
            jedis.setex(skey, timer, svalue);
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }

    }
    /**
     *
     * @param <T>
     * @param mapkey map
     * @param key    map里的key
     * @param requiredType value的泛型类型
     * @return
     */
    public static <T> T getVByMap(String mapkey,String key , Class<T> requiredType){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[] mkey = SerializeUtil.serialize(mapkey);
            byte[] skey = SerializeUtil.serialize(key);
            List<byte[]> result = jedis.hmget(mkey, skey);
            if(null != result && result.size() > 0 ){
                byte[] x = result.get(0);
                T resultObj = SerializeUtil.deserialize(x, requiredType);
                return resultObj;
            }

        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return null;
    }
    /**
     *
     * @param mapkey map
     * @param key    map里的key
     * @param value   map里的value
     */
    public static void setVByMap(String mapkey,String key ,Object value){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[] mkey = SerializeUtil.serialize(mapkey);
            byte[] skey = SerializeUtil.serialize(key);
            byte[] svalue = SerializeUtil.serialize(value);
            jedis.hset(mkey, skey,svalue);
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }

    }
    /**
     * 删除Map里的值
     * @param mapKey
     * @param dkey
     * @return
     */
    public static Object delByMapKey(String mapKey ,String...dkey){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[][] dx = new byte[dkey.length][];
            for (int i = 0; i < dkey.length; i++) {
                dx[i] = SerializeUtil.serialize(dkey[i]);
            }
            byte[] mkey = SerializeUtil.serialize(mapKey);
            Long result = jedis.hdel(mkey, dx);
            return result;
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return new Long(0);
    }

    /**
     * 往redis里取set整个集合
     *
     * @param <T>
     * @param setKey
     * @param requiredType
     * @return
     */
    public static <T> Set<T> getVByList(String setKey,Class<T> requiredType){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[] lkey = SerializeUtil.serialize(setKey);
            Set<T> set = new TreeSet<T>();
            Set<byte[]> xx = jedis.smembers(lkey);
            for (byte[] bs : xx) {
                T t = SerializeUtil.deserialize(bs, requiredType);
                set.add(t);
            }
            return set;
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return null;
    }
    /**
     * 获取Set长度
     * @param setKey
     * @return
     */
    public static Long getLenBySet(String setKey){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            Long result = jedis.scard(setKey);
            return result;
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return null;
    }
    /**
     * 删除Set
     * @param dkey
     * @return
     */
    public static Long delSetByKey(String key,String...dkey){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            Long result = 0L;
            if(null == dkey){
                result = jedis.srem(key);
            }else{
                result = jedis.del(key);
            }
            return result;
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return new Long(0);
    }
    /**
     * 随机 Set 中的一个值
     * @param key
     * @return
     */
    public static String srandmember(String key){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            String result = jedis.srandmember(key);
            return result;
        } catch (Exception e){
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return null;
    }
    /**
     * 往redis里存Set
     * @param setKey
     * @param value
     */
    public static void setVBySet(String setKey,String value){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            jedis.sadd(setKey, value);
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
    }
    /**
     * 取set
     * @param key
     * @return
     */
    public static Set<String> getSetByKey(String key){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            Set<String> result = jedis.smembers(key);
            return result;
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return null;

    }


    /**
     * 往redis里存List
     * @param listKey
     * @param value
     */
    public static void setVByList(String listKey,Object value){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[] lkey = SerializeUtil.serialize(listKey);
            byte[] svalue = SerializeUtil.serialize(value);
            jedis.rpush(lkey, svalue);
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
    }
    /**
     * 往redis里取list
     *
     * @param <T>
     * @param listKey
     * @param start
     * @param end
     * @param requiredType
     * @return
     */
    public static <T> List<T> getVByList(String listKey,int start,int end,Class<T> requiredType){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[] lkey = SerializeUtil.serialize(listKey);
            List<T> list = new ArrayList<T>();
            List<byte[]> xx = jedis.lrange(lkey,start,end);
            for (byte[] bs : xx) {
                T t = SerializeUtil.deserialize(bs, requiredType);
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return null;
    }
    /**
     * 获取list长度
     * @param listKey
     * @return
     */
    public static Long getLenByList(String listKey){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[] lkey = SerializeUtil.serialize(listKey);
            Long result = jedis.llen(lkey);
            return result;
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return null;
    }
    /**
     * 删除
     * @param dkey
     * @return
     */
    public static Long delByKey(String...dkey){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[][] dx = new byte[dkey.length][];
            for (int i = 0; i < dkey.length; i++) {
                dx[i] = SerializeUtil.serialize(dkey[i]);
            }
            Long result = jedis.del(dx);
            return result;
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return new Long(0);
    }
    /**
     * 判断是否存在
     * @param existskey
     * @return
     */
    public static boolean exists(String existskey){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = jedisPool.getResource();
            jedis.select(0);
            byte[] lkey = SerializeUtil.serialize(existskey);
            return jedis.exists(lkey);
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jedis, isBroken);
        }
        return false;
    }
    /**
     * 释放
     * @param jedis
     * @param isBroken
     */
    public static void returnResource(Jedis jedis, boolean isBroken) {
        if (jedis == null)
            return;
        jedis.close();
    }

}
