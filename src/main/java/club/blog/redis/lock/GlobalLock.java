package club.blog.redis.lock;

/**
 * @author chaolei
 */
public interface GlobalLock {

    /**
     * 加锁
     * @param key
     * @return
     */
    boolean lock(String key);

    /**
     * 延迟锁持有时间
     * @param key
     * @return
     */
    boolean delayLock(String key);



    /**
     * 尝试获取锁
     * @param key
     * @return
     */
    boolean tryLock(String key);

    /**
     * 释放锁
     * @param key
     * @return
     */
    boolean unlock(String key);
}
