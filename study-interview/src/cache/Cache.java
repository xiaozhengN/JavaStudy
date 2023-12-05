package cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 缓存接口
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-11-29 22:35:13
 */
public interface Cache<K, V> {
    /**
     * 根据key获取value
     *
     * @param key 缓存key
     * @return 缓存value
     */
    V get(K key);

    /**
     * 获取所有
     *
     * @param keys     缓存key
     * @param result 缓存记录
     */
    @Deprecated
    void getAll(Collection<K> keys, Map<K, V> result);

    /**
     * 新增
     *
     * @param key    缓存key
     * @param value 缓存value
     */
    void put(K key, V value);

    /**
     * 批量新增
     *
     * @param map 缓存记录Map
     */
    void putAll(Map<K, V> map);

    /**
     * 删除, 根据key删除缓存
     *
     * @param key 缓存key
     * @return 已移除的value
     */
    V remove(K key);

    /**
     * 批量删除
     *
     * @param keys 缓存集合
     * @return 已删除的缓存
     */
    Collection<V> removeAll(Collection<K> keys);

    /**
     * 清除缓存
     */
    void clear();

    /**
     * 获取缓存
     *
     * @return 缓存记录
     */
    default Map<K, V> getAll() {
        return Collections.emptyMap();
    }
}
