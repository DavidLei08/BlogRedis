package club.blog.redis;

import club.blog.redis.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogRedisApplication.class, args);

	}


}
