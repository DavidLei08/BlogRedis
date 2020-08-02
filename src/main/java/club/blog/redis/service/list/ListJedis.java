package club.blog.redis.service.list;


import java.util.List;

public interface ListJedis {

    void lpush(String key,List<String> list);

    void lpush(String key,String item);

    void rpush(String key,List<String> list);

    void rpush(String key,String item);

    void lrem(String key,String value);

    String lpop(String key);

    String rpop(String key);

    List<String> get(String key);

    List<String> get(String key,Long endRange);

    List<String> get(String key,Long startRange,Long endRange);

}
