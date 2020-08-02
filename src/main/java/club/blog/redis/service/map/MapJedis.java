package club.blog.redis.service.map;

import java.util.Map;
import java.util.Set;

public interface MapJedis {

    void set(String key,Map<String,String> map);

    void push(String key,Map<String,String> map);

    void push(String key,String mapKey,String mapValue);

    Map<String,String> get(String key);

    Map<String,String> get(String key,Set<String> keySet);

    String get(String key,String mapKey);

    void del(String key);
}
