package club.blog.redis.service.set;

import java.util.Set;

public interface SetJedis {

    void set(String key, Set<String> set);

    void add(String key,Set<String> set);

    void add(String key,String setValue);

    Set<String> get(String key);
}
