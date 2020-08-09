package club.blog.redis.service.string;


import club.blog.redis.lock.annotation.JedisLock;

public interface StringJedis {

    @JedisLock
    void set(String key, String value);

    String get(String key);

    void del(String key);

}
