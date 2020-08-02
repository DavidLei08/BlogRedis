package club.blog.redis.service.set;

import club.blog.redis.service.JedisReturnSource;
import club.blog.redis.util.StringArrayUtil;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.util.Set;

public class SetJedisImppl extends JedisReturnSource implements SetJedis {

    private volatile Jedis jedis;

    private static SetJedisImppl setJedisImppl;

    public static synchronized  SetJedisImppl getInstance(Jedis jedis){
        if(setJedisImppl==null) {
            setJedisImppl = new SetJedisImppl(jedis);
        } else {
            setJedisImppl.jedis = jedis;
            setJedisImppl.lastUseTime = LocalDateTime.now();
        }
        return setJedisImppl;
    }

    private SetJedisImppl(Jedis jedis){
        super();
        this.jedis = jedis;
        this.lastUseTime = LocalDateTime.now();
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

    @Override
    public void set(String key, Set<String> set) {
        if(jedis!=null) {
            jedis.sadd(key, StringArrayUtil.toArray(set));
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public void add(String key, Set<String> set) {
        if(jedis!=null) {
            jedis.sadd(key, StringArrayUtil.toArray(set));
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public void add(String key, String setValue) {
        if(jedis!=null) {
            jedis.sadd(key, setValue);
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public Set<String> get(String key) {
        Set<String> set = null;
        if(jedis!=null) {
            set = jedis.smembers(key);
        }
        return set;
    }
}
