package club.blog.redis.service;

import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;

public abstract class JedisReturnSource {

    protected LocalDateTime lastUseTime;

    public void returnSource(Jedis jedis){
        jedis.close();
    }

    public abstract void returnSource();

    public abstract void del(String key);

    public LocalDateTime getLastUseTime() {
        return this.lastUseTime;
    }

}
