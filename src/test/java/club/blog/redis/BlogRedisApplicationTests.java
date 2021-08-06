package club.blog.redis;

import club.blog.redis.lock.GlobalLock;
import club.blog.redis.service.JedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogRedisApplicationTests {

	@Autowired
	JedisService jedisService;

	@Autowired
	GlobalLock globalLock;

	@Test
	void contextLoads() {
		globalLock.lock("test01");
		jedisService.asString().set("test01", "test01String");
		globalLock.unlock("test01");

		jedisService.asString().get("test01");
		jedisService.asList().lpush("test02", "testList01");
		jedisService.asList().get("test02");
	}

}
