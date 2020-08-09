package club.blog.redis.lock;

import club.blog.redis.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式锁实现
 * @author machenike
 */
@Service
public class GobalLockImpl implements  GobalLock{

    /**
     *
     */
    @Autowired
    private JedisService jedisService;

    private volatile Jedis jedis;

    private Map<String,Long> lockingMap = new ConcurrentHashMap<>();

    @Override
    public boolean lock(String key) {
        String lockKey = getLockKey(key);
        boolean result;
        if(jedis==null){
            jedis = jedisService.getJedis();
        }
        Long time;
        //自旋
        out:for(;;) {
             time = System.currentTimeMillis();
            //尝试取得锁
            result = jedis.setnx(lockKey, time.toString()) > 0;
            //锁取得成功
            if(result){
                //本地存储锁信息
                lockingMap.put(lockKey,time);
                break out;
            }
        }
        return result;
    }

    @Override
    public boolean unlock(String key) {
        String lockKey = getLockKey(key);
        boolean result;
        if(jedis==null){
            jedis = jedisService.getJedis();
        }
        //尝试释放锁
        result = jedis.del(lockKey)>0;
        //锁释放成功
        if(result){
            lockingMap.remove(lockKey);
        }
        return result;
    }

    private String getLockKey(String key){
        return key+".lock";
    }

}
