package club.blog.redis.service;

import club.blog.redis.service.list.ListJedis;
import club.blog.redis.service.map.MapJedis;
import club.blog.redis.service.set.SetJedis;
import club.blog.redis.service.string.StringJedis;
import club.blog.redis.service.zset.ZSetJedis;
import redis.clients.jedis.Jedis;

public interface JedisService {

    /**
     *作为String操作
     * @return
     */
     StringJedis asString();

    /**
     * 作为list操作
     * @return
     */
     ListJedis asList();

    /**
     * 作为Map操作
     * @return
     */
     MapJedis asMap();

    /**
     * 作为set操作
     * @return
     */
     SetJedis asSet();

    /**
     * 作为Zset操作
     * @return
     */
     ZSetJedis asZSet();

     Jedis getJedis();


}
