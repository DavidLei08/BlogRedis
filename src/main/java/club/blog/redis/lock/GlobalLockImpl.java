package club.blog.redis.lock;

import club.blog.redis.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.UUID;

/**
 * 分布式锁实现
 *
 * @author machenike
 */
@Service
public class GlobalLockImpl implements GlobalLock {

    @Value("${club.dlblog.jedis.lock.ex}")
    private Long lockEx;
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

    private long ex = 30;

    @PostConstruct
    public void init() {
        if (lockEx != null) {
            ex = lockEx;
        }
    }

    @Override
    public boolean lock(String key) {
        String lockKey = getLockKey(key);
        if (jedis == null) {
            jedis = jedisService.getJedis();
        }
        String lockVersion = getLockVersion();
        threadLocal.set(lockVersion);
        //自旋
        out:
        for (; ; ) {
            //尝试取得锁 nx不存在则设定成功 px失效
            String result = jedis.set(lockKey, lockVersion, "NX", "PX", ex);
            if (!Objects.isNull(result)) {
                return true;
            }
        }
    }

    @Override
    public boolean delayLock(String key) {
        String lockKey = getLockKey(key);
        if (jedis == null) {
            jedis = jedisService.getJedis();
        }
        String lockVersion = threadLocal.get();
        //判断锁是否为当前线程所持有
        if (lockVersion.equals(jedis.get(lockKey))) {
            jedis.set(lockKey, lockVersion, "XX", "PX", ex);
        }
        return false;
    }

    @Override
    public boolean tryLock(String key) {
        String lockKey = getLockKey(key);
        if (jedis == null) {
            jedis = jedisService.getJedis();
        }
        return !jedis.exists(lockKey);
    }

    @Override
    public boolean unlock(String key) {
        String lockKey = getLockKey(key);
        boolean result;
        if (jedis == null) {
            jedis = jedisService.getJedis();
        }
        //判断锁是否为当前线程所持有
        if (threadLocal.get().equals(jedis.get(lockKey))) {
            //尝试释放锁
            result = jedis.del(lockKey) > 0;
            //锁释放成功
        } else {
            result = true;
        }
        return result;
    }

    private String getLockKey(String key) {
        return "global:lock:" + key;
    }

    private String getLockVersion() {
        String uuid = UUID.randomUUID().toString().replaceAll("_", "");
        uuid += Thread.currentThread().getId();
        return uuid;
    }

}
