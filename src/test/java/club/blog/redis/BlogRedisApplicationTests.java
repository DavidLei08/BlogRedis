package club.blog.redis;

import club.blog.redis.lock.GobalLock;
import club.blog.redis.service.JedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogRedisApplicationTests {

	@Autowired
	JedisService jedisService;

	@Autowired
	GobalLock gobalLock;

	@Test
	void contextLoads() {
		gobalLock.lock("test01");
		jedisService.asString().set("test01", "test01String");
		gobalLock.unlock("test01");

		jedisService.asString().get("test01");
		jedisService.asList().lpush("test02", "testList01");
		jedisService.asList().get("test02");
	}

}
