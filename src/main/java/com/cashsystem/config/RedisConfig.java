package com.cashsystem.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis 配置类
 * 解决两个问题：
 * 1. 默认序列化是 JDK 二进制，存进去的数据人看不懂 → 改成 JSON
 * 2. 开启 Spring Cache 注解支持（@Cacheable 等）
 */
@EnableCaching  // 开启 @Cacheable、@CacheEvict 等注解
@Configuration
public class RedisConfig {

    /**
     * 配置 RedisTemplate
     * 用于手动操作 Redis（opsForValue、opsForHash 等）
     * key 用 String 序列化，value 用 JSON 序列化
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 构建 JSON 序列化器
        Jackson2JsonRedisSerializer<Object> jsonSerializer = buildJsonSerializer();

        // key 和 hashKey 用普通字符串序列化（可读性好）
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // value 和 hashValue 用 JSON 序列化
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置 Spring Cache 管理器
     * 用于 @Cacheable、@CacheEvict 等注解
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Object> jsonSerializer = buildJsonSerializer();

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 默认缓存过期时间 10 分钟
                .entryTtl(Duration.ofMinutes(10))
                // key 用字符串序列化
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // value 用 JSON 序列化
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                // 不缓存 null 值
                .disableCachingNullValues();

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }

    /**
     * 构建 Jackson JSON 序列化器
     * 支持 Java 8 时间类型（LocalDateTime 等）
     * 支持多态类型信息（反序列化时能还原成正确的类型）
     */
    private Jackson2JsonRedisSerializer<Object> buildJsonSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        // 允许序列化所有访问级别的字段
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 存储类型信息，反序列化时能还原成正确的对象类型
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        // 支持 LocalDateTime 等 Java 8 时间类型
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonRedisSerializer<>(mapper, Object.class);
    }
}
