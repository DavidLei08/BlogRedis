package club.blog.redis.service.string;


public interface StringJedis {

    void set(String key, String value);

    String get(String key);

    void del(String key);

}
