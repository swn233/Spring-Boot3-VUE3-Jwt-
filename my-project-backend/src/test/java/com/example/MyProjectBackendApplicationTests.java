package com.example;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class MyProjectBackendApplicationTests {
	@Resource
	StringRedisTemplate template;

	@Test
	void contextLoads() {
		template.opsForValue().set("a","888");
		System.out.println(template.opsForValue().get("a"));

	}

}
