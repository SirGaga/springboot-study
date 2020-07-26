package com.zhangjie.springbootstudy.service.impl;

import com.zhangjie.springbootstudy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private JedisPool jedisPool;

    @Override
    public String getString(String key) {
        // 得到jedis对象
        try( Jedis jedis = jedisPool.getResource() ){
            // 判断key是否在redis中存在
            if (jedis.exists(key)){
                logger.info("查询redis中的key："+key);
                return jedis.get(key);
            }else{
                logger.info("查询mysql中的数据");
                String val = "1";
                jedis.set(key,val);
                return val;
            }
        }




    }
}
