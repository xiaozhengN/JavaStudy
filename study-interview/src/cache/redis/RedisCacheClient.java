package cache.redis;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-14 21:25:52
 */
@Component
public class RedisCacheClient implements CacheClient{
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void set(Object key, Object value, long timeOut) {
        redisTemplate.opsForValue().set(key, value, timeOut, TimeUnit.MINUTES);
    }

    @Override
    public Object get(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void remove(Object key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delelteByPrefix(String prefix) {
        String patternStr = prefix + "*";
        Set<Object> keys = redisTemplate.keys(patternStr);
        if (CollectionUtil.isNotEmpty(keys)) {
            redisTemplate.keys(keys);
        }
    }
}
