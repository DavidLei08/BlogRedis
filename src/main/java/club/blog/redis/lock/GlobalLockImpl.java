package club.blog.redis.lock;

import club.blog.redis.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * 分布式锁实现
 * @author machenike
 */
@Service
public class GlobalLockImpl implements  GlobalLock{

    /**
     * jedis实例管理服务
     */
    @Autowired
    private JedisService jedisService;

    /**
     * jedis实例
     */
    private volatile Jedis jedis;

    /**
     * threadLocal当前线程持有
     */
    private volatile ThreadLocal<String> threadLocal = new ThreadLocal<>();



    @Override
    public boolean lock(String key) {
        String lockKey = getLockKey(key);
        boolean result;
        if(jedis==null){
            jedis = jedisService.getJedis();
        }
        String lockVersion = getLockVersion();
        threadLocal.set(lockVersion);
        //自旋
        out:for(;;) {
            //尝试取得锁
            result = jedis.setnx(lockKey, lockVersion)>0;
            //锁取得成功
            if(result){
                //设置过期时间，防止死锁
                jedis.expire(lockKey, 30);
                //本地存储锁信息
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
        //判断锁是否为当前线程所持有
        if(jedis.get(lockKey).equals(threadLocal.get())){
            //尝试释放锁
            result = jedis.del(lockKey)>0;
            //锁释放成功
        } else {
            result = false;
        }
        return result;
    }

    private String getLockKey(String key){
        return key+".global.lock";
    }

    private String getLockVersion(){
        String uuid = UUID.randomUUID().toString().replaceAll("_", "");
        uuid+=Thread.currentThread().getId();
        return uuid;
    }

}
