package club.blog.redis.service.string;

import club.blog.redis.lock.annotation.JedisLock;
import club.blog.redis.service.JedisReturnSource;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;

/**
 * @author machenike
 */
public class StringJedisImpl  extends JedisReturnSource implements StringJedis {

    private volatile static StringJedisImpl stringJedisImpl;

    private volatile Jedis jedis;

    public static synchronized StringJedisImpl getInstance(Jedis jedis){
        if(stringJedisImpl==null){
            stringJedisImpl = new StringJedisImpl(jedis);
        } else {
            stringJedisImpl.jedis = jedis;
            stringJedisImpl.lastUseTime = LocalDateTime.now();
        }
        return  stringJedisImpl;
    }

    private StringJedisImpl(Jedis jedis){
        super();
        this.jedis = jedis;
        this.lastUseTime = LocalDateTime.now();
    }

    @JedisLock
    @Override
    public void set(String key, String value) {
        if(jedis!=null&&jedis.isConnected()){
            jedis.set(key, value);
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public String get(String key) {
        String value = null;
        if(jedis!=null&&jedis.isConnected()){
            value = jedis.get(key);
            this.lastUseTime = LocalDateTime.now();
        }
        return value;
    }

    @Override
    public void returnSource() {
        returnSource(jedis);
    }

    @Override
    public void del(String key) {
        if(jedis!=null&&jedis.isConnected()){
           jedis.del(key);
            this.lastUseTime = LocalDateTime.now();
        }
    }
}
