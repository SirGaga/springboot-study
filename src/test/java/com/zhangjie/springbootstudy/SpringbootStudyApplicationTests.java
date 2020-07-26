package com.zhangjie.springbootstudy;

import com.zhangjie.springbootstudy.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisPool;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootStudyApplicationTests {
	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private UserService userService;

	@Test
	public void contextLoads() {
		System.out.println(jedisPool);
	}

	/**
	 * 模拟jedis从redis中判断key、取值key、设置key-value的string值
	 */
	@Test
	public void test(){
		String result = userService.getString("name");
		System.out.println(result);
	}

}
