package cache;

import java.util.Collection;
import java.util.Map;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-05 22:20:26
 */
public interface CacheDataLoader<K, V> {
    /**
     * 加载key
     *
     * @param key 加载的key
     * @return V
     */
    V load(K key);

    /**
     * 批量加载
     *
     * @param keys key集合
     * @param records 需要加载的K, V键值对
     */
    void loadAll(Collection<K> keys, Map<K, V> records);
}
