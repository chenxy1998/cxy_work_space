package com.cxy.user.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig {
    /**
     * Redis服务器地址
     **/
    /*@Value("${spring.redis.host}")
    private String host;*/
    /**
     * Redis服务器连接端口
     **/
    /*@Value("${spring.redis.port}")
    private Integer port;*/
    @Value("${spring.redis.nodes}")
    private String nodes;
    /**
     * Redis数据库索引（默认为0）
     **/
    @Value("${spring.redis.database}")
    private Integer database;
    /**
     * Redis服务器连接密码（默认为空）
     **/
    @Value("${spring.redis.password}")
    private String password;
    /**
     * 连接超时时间（毫秒）
     **/
    @Value("${spring.redis.timeout}")
    private Integer timeout;
    /**
     * 连接池最大连接数（使用负值表示没有限制）
     **/
    @Value("${spring.redis.lettuce.pool.max-active}")
    private Integer maxTotal;
    /**
     * 连接池最大阻塞等待时间（使用负值表示没有限制）
     **/
    @Value("${spring.redis.lettuce.pool.max-wait}")
    private Integer maxWait;
    /**
     * 连接池中的最大空闲连接
     **/
    @Value("${spring.redis.lettuce.pool.max-idle}")
    private Integer maxIdle;
    /**
     * 连接池中的最小空闲连接
     **/
    @Value("${spring.redis.lettuce.pool.min-idle}")
    private Integer minIdle;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(getConnectionFactory());

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用 String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的 key也采用 String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用 jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的 value序列化方式采用 jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * @Description 获取缓存连接
     * @Author 王栋
     * @Date 2019/4/20 23:37
     * @param:
     * @return org.springframework.data.redis.connection.RedisConnectionFactory
     **/
    @Bean
    public LettuceConnectionFactory getConnectionFactory() {
        //单机模式
        //RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        //哨兵模式
        //RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
        //集群模式
        RedisClusterConfiguration configuration = new RedisClusterConfiguration();


        /*configuration.setHostName(host);
        configuration.setPort(port);
        // 默认 0
        configuration.setDatabase(database);*/
        String[] split = nodes.split(",");
        ArrayList<RedisNode> objects = new ArrayList<>();

        for (String s : split) {
            String[] split1 = s.split(":");
            RedisNode redisNode = new RedisNode(split1[0], Integer.valueOf(split1[1]));
            objects.add(redisNode);
        }
        configuration.setClusterNodes(objects);
        configuration.setPassword(RedisPassword.of(password));

        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, getPoolConfig());
        //factory.setShareNativeConnection(false);//是否允许多个线程操作共用同一个缓存连接，默认true，false时每个操作都将开辟新的连接
        return factory;
    }

    /**
     * @Description 获取缓存连接池
     * @Author 王栋
     * @Date 2019/4/20 23:36
     * @param:
     * @return org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration
     **/
    @Bean
    public LettucePoolingClientConfiguration getPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxWaitMillis(maxWait);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        LettucePoolingClientConfiguration pool = LettucePoolingClientConfiguration.builder()
                .poolConfig(config)
                .commandTimeout(Duration.ofMillis(timeout))
                .build();
        return pool;
    }
}
