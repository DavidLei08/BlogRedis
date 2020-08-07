package club.blog.redis.service.zset;

import java.util.Set;

public interface ZSetJedis {

    Set<String> get(String key);

   Set<String> get(String key,Long end);

   Set<String> get(String key,Long start,Long end);

   void zadd(String key,String value);

    void zadd(String key,String value,Long score);

    void zrem(String key,String value);

    void zrem(String key,Set<String> values);
}
