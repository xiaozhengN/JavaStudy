package cache.redis;

/**
 * CacheClient
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-14 21:10:49
 */
public interface CacheClient {
    /**
     * 插入缓存
     *
     * @param key     键
     * @param value   值
     * @param timeOut 过期时间
     */
    void set(Object key, Object value, long timeOut);

    /**
     * 通过key获取缓存value
     *
     * @param key 缓存key
     * @return 缓存value
     */
    Object get(Object key);

    /**
     * 移除指定缓存
     *
     * @param key 缓存key
     */
    void remove(Object key);

    /**
     * 通过key前缀移除缓存
     *
     * @param prefix 缓存key的前缀
     */
    void delelteByPrefix(String prefix);
}
