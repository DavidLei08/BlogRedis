![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly93d3cuZGxibG9nLmNsdWIvZmlsZS8yMDIwMDQyNS9welBtaUhzay5wbmc?x-oss-process=image/format,png)
# BlogRedis

## 完全基于Jedis，使用spring-boot集成
```xml
		<!-- 引入Jedis-->
		<dependency>
			<groupId>redis.clients</groupId>
			<artifactId>jedis</artifactId>
			<version>2.5.2</version>
		</dependency>
```
## 支持以下数据类型
|  | 数据类型 |使用方式|
|--|--|--|
| 1 | String |jedisService.asString().set(...)|
| 2 | List |jedisService.asList().lpush(...)|
| 3 | Map |jedisService.asMap().set(...)|
| 4 | Set |jedisService.asSet().set(...)|
| 5 | Zet |jedisService.asZSet().set(...)|

## 支持自定义配置

```yml
#BlogRedis配置
club.dlblog.jedis:
   host: 127.0.0.1 #ip地址
   port: 6379 #端口号
   auth: a23456 #认证密码
   maxActive: 1024 #最大连接数
   maxIdle: 200 #最大闲置连接数
   maxWait: 10000 #最长等待时间
   timeOut: 10000 #连接超时时间
   testOnBorrow: true #连接验证
   defaultDataBase: 0 #默认db
   clientName: dlblog-jedis-client #jedis客户端名
   auto.destory.ms: 10000 #自动回收时间ms
```   

> jedisPool使用重量级锁保证线程安全，全局调用jedisPool生成jedis实例的地方并不多，并发不会太多，不会影响效率

## 新功能实现
基于redis源生指令的全局分布式锁
```java
                //获取锁
		gobalLock.lock("test01");
		jedisService.asString().set("test01", "test01String");
		//释放锁
		gobalLock.unlock("test01");
```
参照CAS的轻量锁，尝试取锁，取不到锁时内部会自旋等待下一次取锁

```java
/**
 * 分布式锁实现
 * @author machenike
 */
@Service
public class GobalLockImpl implements  GobalLock{

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
     * 线程安全锁信息存储用Map
     */
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
```

## 新增分布式锁注解
```java
    @JedisLock
    public void update(@LockKey String key,String value) {
    }
```
