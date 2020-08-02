package club.blog.redis.service.list;

import club.blog.redis.service.JedisReturnSource;
import club.blog.redis.util.StringArrayUtil;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author machenike
 */
public class ListJedisImpl extends JedisReturnSource implements  ListJedis {

    private volatile  Jedis jedis;

    private static ListJedisImpl listJedisImpl;

    public static synchronized  ListJedisImpl getInstance(Jedis jedis){
        if(listJedisImpl==null) {
            listJedisImpl = new ListJedisImpl(jedis);
        } else {
            listJedisImpl.jedis = jedis;
            listJedisImpl.lastUseTime = LocalDateTime.now();
        }
        return listJedisImpl;
    }

    private ListJedisImpl(Jedis jedis){
        super();
        this.jedis = jedis;
        this.lastUseTime = LocalDateTime.now();
    }

    @Override
    public void lpush(String key, List<String> list) {
        if(jedis!=null){
            jedis.lpush(key, StringArrayUtil.toArray(list));
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public void lpush(String key, String item) {
        if(jedis!=null){
            jedis.lpush(key, item);
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public void rpush(String key, List<String> list) {
        if(jedis!=null){
            jedis.rpush(key, StringArrayUtil.toArray(list));
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public void rpush(String key, String item) {
        if(jedis!=null){
            jedis.rpush(key, item);
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public void lrem(String key, String value) {
        if(jedis!=null){
            jedis.lrem(key, 0L,value);
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public String lpop(String key) {
        String item = null;
        if(jedis!=null){
            item = jedis.lpop(key);
            this.lastUseTime = LocalDateTime.now();
        }
        return item;
    }

    @Override
    public String rpop(String key) {
        String item = null;
        if(jedis!=null){
            item = jedis.rpop(key);
            this.lastUseTime = LocalDateTime.now();
        }
        return item;
    }

    @Override
    public List<String> get(String key) {
        List<String> list = new ArrayList<>();
        if(jedis!=null) {
            list = jedis.lrange(key, 0, -1);
            this.lastUseTime = LocalDateTime.now();
        }
        return list;
    }

    @Override
    public List<String> get(String key, Long endRange) {
        List<String> list = new ArrayList<>();
        if(jedis!=null) {
            list = jedis.lrange(key, 0, endRange);
            this.lastUseTime = LocalDateTime.now();
        }
        return list;
    }

    @Override
    public List<String> get(String key, Long startRange, Long endRange) {
        List<String> list = new ArrayList<>();
        if(jedis!=null) {
            list = jedis.lrange(key, startRange, endRange);
            this.lastUseTime = LocalDateTime.now();
        }
        return list;
    }


    @Override
    public void returnSource() {
        returnSource(jedis);
    }

    @Override
    public void del(String key) {
        if(jedis!=null) {
            jedis.del(key);
            this.lastUseTime = LocalDateTime.now();
        }
    }

}
