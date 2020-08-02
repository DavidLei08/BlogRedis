package club.blog.redis.service.map;

import club.blog.redis.service.JedisReturnSource;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class MapJedisImpl extends JedisReturnSource implements MapJedis {

    private volatile Jedis jedis;

    private static MapJedisImpl mapJedisImpl;

    public static synchronized  MapJedisImpl getInstance(Jedis jedis){
        if(mapJedisImpl==null) {
            mapJedisImpl = new MapJedisImpl(jedis);
        } else {
            mapJedisImpl.jedis = jedis;
            mapJedisImpl.lastUseTime = LocalDateTime.now();
        }
        return mapJedisImpl;
    }

    private MapJedisImpl(Jedis jedis){
        super();
        this.jedis = jedis;
        this.lastUseTime = LocalDateTime.now();
    }

    @Override
    public void returnSource() {
        returnSource(jedis);
    }

    @Override
    public void set(String key, Map<String, String> map) {
        if(jedis!=null) {
            Set<String> keySet = map.keySet();
            for (String mapKey : keySet) {
                jedis.hset(key, mapKey, map.get(mapKey));
            }
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public void push(String key, Map<String, String> map) {
        if(jedis!=null) {
            Set<String> keySet = map.keySet();
            for (String mapKey : keySet) {
                jedis.hset(key, mapKey, map.get(mapKey));
            }
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public void push(String key, String mapKey, String mapValue) {
        if(jedis!=null) {
            jedis.hset(key, mapKey, mapValue);
            this.lastUseTime = LocalDateTime.now();
        }
    }

    @Override
    public Map<String, String> get(String key) {
        Map<String ,String> map = null;
        if(jedis!=null) {
            map = jedis.hgetAll(key);
            this.lastUseTime = LocalDateTime.now();
        }
        return map;
    }

    @Override
    public Map<String, String> get(String key, Set<String> keySet) {
        Map<String ,String> map = null;
        if(jedis!=null) {
            Map<String ,String>  tempMap = jedis.hgetAll(key);
            for(String mapKey:keySet){
                map.put(mapKey, tempMap.get(mapKey));
            }
            this.lastUseTime = LocalDateTime.now();
        }
        return map;
    }

    @Override
    public String get(String key, String mapKey) {
        String value = null;
        if(jedis!=null) {
            value = jedis.hget(key,mapKey);
            this.lastUseTime = LocalDateTime.now();
        }
        return value;
    }

    @Override
    public void del(String key) {
        if(jedis!=null) {
            jedis.del(key);
            this.lastUseTime = LocalDateTime.now();
        }
    }
}
