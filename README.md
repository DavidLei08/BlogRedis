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

## 代码一览
```yml
----config
|       JedisConfig.java
|
+---service
|   |   JedisReturnSource.java
|   |   JedisService.java
|   |
|   +---impl
|   |       JedisServiceImpl.java
|   |
|   +---list
|   |       ListJedis.java
|   |       ListJedisImpl.java
|   |
|   +---map
|   |       MapJedis.java
|   |
|   +---set
|   |       SetJedis.java
|   |
|   +---string
|   |       StringJedis.java
|   |       StringJedisImpl.java
|   |
|   |---zset
|           ZSetJedis.java
|
|---util
        StringArrayUtil.java
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

