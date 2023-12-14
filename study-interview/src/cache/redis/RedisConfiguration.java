package cache.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis相关配置
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-14 21:30:20
 */
@Configuration
@EnableCaching
public class RedisConfiguration {
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 键序列化
        redisTemplate.setKeySerializer(getKeySerializer());
        redisTemplate.setHashKeySerializer(getKeySerializer());
        // 值序列化
        redisTemplate.setValueSerializer(getValueSerializer());
        redisTemplate.setHashValueSerializer(getValueSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private RedisSerializer<Object> getValueSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        // 将类名序列化到JSON中
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 设置输入时, 忽略JSON字符串中存在而Java实体类中没有的属性
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

    private RedisSerializer<String> getKeySerializer() {
        return new StringRedisSerializer();
    }
}
