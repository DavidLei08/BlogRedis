package club.blog.redis.service.impl;

import club.blog.redis.service.JedisService;
import club.blog.redis.service.list.ListJedis;
import club.blog.redis.service.list.ListJedisImpl;
import club.blog.redis.service.map.MapJedis;
import club.blog.redis.service.map.MapJedisImpl;
import club.blog.redis.service.set.SetJedis;
import club.blog.redis.service.set.SetJedisImppl;
import club.blog.redis.service.string.StringJedis;
import club.blog.redis.service.string.StringJedisImpl;
import club.blog.redis.service.zset.ZSetJedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * jedis服务
 */
@EnableScheduling
@Service
public class JedisServiceImpl implements JedisService {

    /**
     * jedis连接池
     */
    @Autowired
    private  volatile  JedisPool jedisPool;

    /**
     * 字符串jedis模板
     */
    private StringJedis stringJedis;

    /**
     * listjedis模板
     */
    private ListJedis listJedis;

    /**
     * Mapjedis模板
     */
    private MapJedis mapJedis;

    /**
     * set模板
     */
    private SetJedis setJedis;

    /**
     * zset模板
     */
    private ZSetJedis zSetJedis;

    /**
     * 自动回收时间差
     */
    @Value("${club.dlblog.jedis.auto.destory.ms}")
    private Long autoDestoryMs;

    /**
     * 使用字符串模板
     * @return
     */
    @Override
    public StringJedis asString() {
            stringJedis = StringJedisImpl.getInstance(getJedis());
        return  stringJedis;
    }

    /**
     * 使用list模板
     * @return
     */
    @Override
    public ListJedis asList() {
        listJedis  = ListJedisImpl.getInstance(getJedis());
        return listJedis;
    }

    /**
     * 使用map模板
     * @return
     */
    @Override
    public MapJedis asMap() {
        mapJedis = MapJedisImpl.getInstance(getJedis());
        return mapJedis;
    }

    /**
     * 使用set模板
     * @return
     */
    @Override
    public SetJedis asSet() {
        setJedis = SetJedisImppl.getInstance(getJedis());
        return setJedis;
    }

    /**
     * 使用zset模板
     * @return
     */
    @Override
    public ZSetJedis asZSet() {
        return null;
    }

    /**
     * jedis实例取得
     * @return
     */
    @Override
    public synchronized  Jedis getJedis() {
        Jedis jedis  =null;
        if(jedisPool!=null){
            jedis = jedisPool.getResource();
        }
        return jedis;
    }

    /**
     * 自动回收jedis实例
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void autoReturnSource(){
        if(stringJedis instanceof StringJedisImpl){
            StringJedisImpl jedisImpl =  (StringJedisImpl)stringJedis;
           if(isNeedDestory(jedisImpl.getLastUseTime())) {
               jedisImpl.returnSource();
           }
        }

        if(listJedis instanceof ListJedisImpl){
            ListJedisImpl jedisImpl =  (ListJedisImpl)stringJedis;
            if(isNeedDestory(jedisImpl.getLastUseTime())) {
                jedisImpl.returnSource();
            }
        }
    }

    private boolean isNeedDestory(LocalDateTime time){
        LocalDateTime now = LocalDateTime.now();
        Long nowMs =  now.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        Long oldMs = time.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        Long realTime = nowMs-oldMs;
        if(realTime>autoDestoryMs){
            return true;
        }
        return false;
    }

}
