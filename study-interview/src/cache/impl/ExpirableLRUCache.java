package cache.impl;

import cache.Cache;
import cache.CacheDataLoader;
import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ExpirableLRUCache
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-10 12:47:15
 */
public class ExpirableLRUCache<K, V> implements Cache<K, V> {

    private final Cache<K, ExpirableObject<V>> backCache;

    /**
     * 访问超期时间
     */
    private final int accessExpMills;

    /**
     * 数据失效时间(写超时时间, 数据如果不写, 只读的话, 一段时间后就失效)
     */
    private final int writeExpMills;

    /**
     * 构造方法
     *
     * @param loader         加载器
     * @param size           容量
     * @param accessExpMills 访问超期时间
     * @param writeExpMills  数据失效时间
     */
    public ExpirableLRUCache(CacheDataLoader<K, V> loader, int size, int accessExpMills, int writeExpMills) {
        this.backCache = new LRUCache<>(new CacheDataLoader<K, ExpirableObject<V>>() {
            @Override
            public ExpirableObject<V> load(K key) {
                V vLoad = loader.load(key);
                ExpirableObject<V> newLoadObj = null;
                if (vLoad == null) {
                    newLoadObj = new ExpirableObject<>(vLoad);
                }
                return newLoadObj;
            }

            @Override
            public void loadAll(Collection<K> keys, Map<K, ExpirableObject<V>> records) {
                Map<K, V> originRecords = new HashMap<>(keys.size());
                loader.loadAll(keys, originRecords);
                originRecords.forEach((key, value) -> {
                    ExpirableObject<V> newLoadObj = new ExpirableObject<>(value);
                    records.put(key, newLoadObj);
                });
            }
        }, size);
        this.accessExpMills = accessExpMills;
        this.writeExpMills = writeExpMills;
    }

    @Override
    public V get(K key) {
        ExpirableObject<V> expObject = backCache.get(key);
        if (expObject == null) {
            return null;
        }
        long curTime = System.currentTimeMillis();

        if (isObjectExpired(expObject, curTime)) {
            backCache.remove(key);
            return null;
        }
        expObject.lastAccessTime = System.currentTimeMillis();
        return expObject.value;
    }

    /**
     * 判断缓存的对象是否过期
     *
     * @param expObject 已缓存的对象
     * @param curTime   系统当前时间
     * @return 判断缓存的对象是否过期的结果, 如果过期返回true
     */
    private boolean isObjectExpired(ExpirableObject<V> expObject, long curTime) {
        if (accessExpMills > 0 && (curTime - expObject.lastAccessTime > accessExpMills)) {
            return true;
        }
        if (writeExpMills > 0) {
            return curTime - expObject.lastAccessTime > writeExpMills;
        }
        return false;
    }

    @Override
    public void getAll(Collection<K> keys, Map<K, V> result) {
        Map<K, ExpirableObject<V>> expObjects = new HashMap<>();
        backCache.getAll(keys, expObjects);
        long curTime = System.currentTimeMillis();
        expObjects.forEach((k, v) -> {
            if (isObjectExpired(v, curTime)) {
                backCache.remove(k);
                return;
            }
            v.lastAccessTime = curTime;
            result.put(k, v.value);
        });
    }

    @Override
    public void put(K key, V value) {
        ExpirableObject<V> expObject = new ExpirableObject<>(value);
        backCache.put(key, expObject);
    }

    @Override
    public void putAll(Map<K, V> records) {
        Map<K, ExpirableObject<V>> expObjects = new HashMap<>(records.size());
        long curTime = System.currentTimeMillis();
        records.forEach((k, v) -> {
            ExpirableObject<V> expObject = new ExpirableObject<>(v, curTime);
            expObjects.put(k, expObject);
        });
        backCache.putAll(expObjects);
    }

    @Override
    public V remove(K key) {
        ExpirableObject<V> vExpirableObject = backCache.remove(key);
        return vExpirableObject == null ? null : vExpirableObject.value;
    }

    @Override
    public Collection<V> removeAll(Collection<K> keys) {
        Collection<ExpirableObject<V>> expirableObjects = backCache.removeAll(keys);
        return expirableObjects.stream().map(vExpirableObject -> vExpirableObject.value).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        backCache.clear();
    }

    /**
     * ExpirableObject
     *
     * @param <V> 泛型
     */
    @Data
    static class ExpirableObject<V> {
        /**
         * 最后访问时间
         */
        private long lastAccessTime;

        /**
         * 最后写入时间
         */
        private long lastWriteTime;

        /**
         * Value值
         */
        private V value;

        /**
         * 指定时间的有参构造
         *
         * @param value      值
         * @param accessTime 访问时间
         */
        public ExpirableObject(V value, long accessTime) {
            this.value = value;
            this.lastAccessTime = accessTime;
            this.lastWriteTime = accessTime;
        }

        /**
         * 有参构造
         *
         * @param value 值
         */
        public ExpirableObject(V value) {
            this(value, System.currentTimeMillis());
        }
    }
}
