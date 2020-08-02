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

后续将继续更新，保证操作便捷的同时保证运行效率，同时将会考虑线程安全的问题
