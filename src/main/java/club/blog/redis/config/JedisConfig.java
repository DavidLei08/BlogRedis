package club.blog.redis.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis配置类
 * @author machenike
 */
@Configuration
public class JedisConfig {

    /**
     * 服务器IP地址
     */
    @Value("${club.dlblog.jedis.host}")
    private  String host;

    /**
     * 端口
     */
    @Value("${club.dlblog.jedis.port}")
    private Integer port;

    /**
     * 密码
     */
    @Value("${club.dlblog.jedis.auth}")
    private String auth;

    /**
     * 连接实例的最大连接数
     */
    @Value("${club.dlblog.jedis.maxActive}")
    private Integer maxActive;

    /**
     * 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
     */
    @Value("${club.dlblog.jedis.maxIdle}")
    private Integer maxIdle;

    /**
     * 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间
     */
    @Value("${club.dlblog.jedis.maxWait}")
    private Integer maxWait;

    /**
     * 连接超时的时间　
     */
    @Value("${club.dlblog.jedis.timeOut}")
    private Integer timeOut;

    /**
     * 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
     */
    @Value("${club.dlblog.jedis.testOnBorrow}")
    private boolean testOnBorrow;

    /**
     * 数据库模式是16个数据库 0~15
     */
    @Value("${club.dlblog.jedis.defaultDataBase}")
    private Integer defaultDataBase;

    /**
     * redis连接客户端名称
     */
    @Value("${club.dlblog.jedis.clientName}")
    private String  clientName;

    /**
     * 初始jedisPool配置
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        if(maxActive!=null) {
            jedisPoolConfig.setMaxTotal(maxActive);
        } else {
            jedisPoolConfig.setMaxTotal(1024);
        }
        if(maxIdle!=null){
            jedisPoolConfig.setMaxIdle(maxIdle);
        } else {
            jedisPoolConfig.setMaxIdle(200);
        }
        if(maxWait!=null){
            jedisPoolConfig.setMaxWaitMillis(maxWait);
        }else {
            jedisPoolConfig.setMaxWaitMillis(10000);
        }
        if(maxWait!=null) {
            jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        } else {
            jedisPoolConfig.setTestOnBorrow(true);
        }
        return  jedisPoolConfig;
    }

    /**
     * 初始化jedisPool
     * @param jedisPoolConfig
     * @return
     */
    @Bean
    public JedisPool jedisPool(@Qualifier("jedisPoolConfig")JedisPoolConfig  jedisPoolConfig){
        if(StringUtils.isEmpty(auth)){
            auth = null;
        }
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host,port,timeOut,auth,defaultDataBase,clientName);
        return jedisPool;
    }

}
