package cache;

import exception.BusinessException;

import java.util.Collection;
import java.util.Map;

/**
 * CaheDataStore
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-05 22:23:51
 */
public interface CacheDataStore<K, V> {
    /**
     * 初始化
     *
     * @throws BusinessException 业务异常
     */
    void init() throws BusinessException;

    /**
     * 加价key
     *
     * @param key 缓存key
     * @return 缓存value
     * @throws BusinessException 业务异常
     */
    V load(K key) throws BusinessException;

    /**
     * 批量加载缓存
     *
     * @param key 缓存key
     * @param record 缓存记录
     * @throws BusinessException 业务异常
     */
    void loadAll(Collection<K> key, Map<K, V> record) throws BusinessException;

    /**
     * 保存缓存
     *
     * @param key 缓存key
     * @param record 缓存value
     * @throws BusinessException 业务异常
     */
    void save(K key, V record) throws BusinessException;

    /**
     * 批量保存
     *
     * @param records 缓存记录Map
     * @throws BusinessException 业务异常
     */
    void saveAll(Map<K, V> records) throws BusinessException;

    /**
     * 根据Key移除缓存
     *
     * @param key 缓存key
     * @return 已移除的value
     * @throws BusinessException 业务异常
     */
    V remove(K key) throws BusinessException;

    /**
     * 批量移除缓存
     *
     * @param keys 缓存key集合
     * @return 已移除的缓存value集合
     * @throws BusinessException 业务异常
     */
    Collection<V> removeAll(Collection<K> keys) throws BusinessException;

    /**
     * 清除缓存
     *
     * @throws BusinessException 业务异常
     */
    void clear() throws BusinessException;
}
