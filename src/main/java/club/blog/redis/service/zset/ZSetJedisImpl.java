package club.blog.redis.service.zset;

import club.blog.redis.service.JedisReturnSource;
import club.blog.redis.util.StringArrayUtil;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author machenike
 */
public class ZSetJedisImpl extends JedisReturnSource implements  ZSetJedis {

    private volatile static ZSetJedisImpl zSetJedisImpl;

    private volatile Jedis jedis;

    public static synchronized ZSetJedisImpl getInstance(Jedis jedis){
        if(zSetJedisImpl==null){
            zSetJedisImpl = new ZSetJedisImpl(jedis);
        } else {
            zSetJedisImpl.jedis = jedis;
            zSetJedisImpl.lastUseTime = LocalDateTime.now();
        }
        return  zSetJedisImpl;
    }

    private ZSetJedisImpl(Jedis jedis){
        super();
        this.jedis = jedis;
        this.lastUseTime = LocalDateTime.now();
    }

    @Override
    public void returnSource() {
        returnSource(jedis);
    }

    @Override
    public Set<String> get(String key){
        Set<String> set = null;
        if(jedis!=null){
            set =jedis.zrange(key, 0L, -1L);
        }
        this.lastUseTime = LocalDateTime.now();
        return  set;
    }

    @Override
    public Set<String> get(String key, Long end){
        Set<String> set = null;
        if(jedis!=null){
            set =jedis.zrange(key, 0L, end);
        }
        this.lastUseTime = LocalDateTime.now();
        return  set;
    }

    @Override
    public Set<String> get(String key, Long start, Long end){
        Set<String> set = null;
        if(jedis!=null){
            set =jedis.zrange(key, start, end);
        }
        this.lastUseTime = LocalDateTime.now();
        return  set;
    }

    @Override
    public void zadd(String key, String value){
        if(jedis!=null){
            Long lastScore = jedis.zcard(key);
            jedis.zadd(key, lastScore+1, value);
        }
        this.lastUseTime = LocalDateTime.now();
    }


    @Override
    public void zadd(String key, String value, Long score){
        if(jedis!=null){
            jedis.zadd(key, score, value);
        }
        this.lastUseTime = LocalDateTime.now();
    }

    @Override
    public void zrem(String key, String value){
        if(jedis!=null){
            jedis.zrem(key, value);
        }
        this.lastUseTime = LocalDateTime.now();
    }

    @Override
    public void zrem(String key, Set<String> values){
        if(jedis!=null){
            jedis.zrem(key, StringArrayUtil.toArray(values));
        }
        this.lastUseTime = LocalDateTime.now();
    }





    @Override
    public void del(String key) {
        if(jedis!=null) {
            jedis.del(key);
            this.lastUseTime = LocalDateTime.now();
        }
    }
}
