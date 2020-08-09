package club.blog.redis.lock;

public interface GobalLock {

    /**
     * 加锁
     * @param key
     * @return
     */
    boolean lock(String key);

    /**
     * 释放锁
     * @param key
     * @return
     */
    boolean unlock(String key);
}
