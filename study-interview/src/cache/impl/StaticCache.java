package cache.impl;

import cache.Cache;
import cache.CacheDataLoader;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StaticCache
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-14 20:57:42
 */
public class StaticCache<K, V> implements Cache<K, V> {
    private CacheDataLoader<K, V> loader;

    private Map<K, V> data = new ConcurrentHashMap<>();

    StaticCache(CacheDataLoader<K, V> loader) {
        if (loader == null) {
            throw new IllegalArgumentException("Null CacheDataLoader");
        }
        this.loader = loader;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }

        V value = this.data.get(key);

        if (value == null) {
            value = this.loader.load(key);
            put(key, value);
        }

        return value;
    }

    @Override
    public void getAll(Collection<K> keys, Map<K, V> result) {
        List<K> missingKeys = new ArrayList<>();

        for (K key : keys) {
            V value = this.data.get(key);
            if (value != null) {
                result.put(key, value);
            } else {
                missingKeys.add(key);
            }
        }

        if (missingKeys.size() <= 0) {
            return;
        }

        Map<K, V> loaded = new HashMap<>();
        this.loader.loadAll(missingKeys, loaded);
        result.putAll(loaded);
        putAll(loaded);
    }

    @Override
    public void put(K key, V value) {
        if (key == null || value == null) {
            return;
        }
        this.data.put(key, value);
    }

    @Override
    public void putAll(Map<K, V> map) {
        if (map == null || map.size() == 0) {
            return;
        }

        Set<Map.Entry<K, V>> entrySet = map.entrySet();
        for (Map.Entry<K, V> entry : entrySet) {
            this.data.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        return this.data.remove(key);
    }

    @Override
    public Collection<V> removeAll(Collection<K> keys) {
        List<V> removed = new ArrayList<>(keys.size());
        for (K key : keys) {
            V value = remove(key);
            if (value != null) {
                removed.add(value);
            }
        }
        return removed;
    }

    @Override
    public void clear() {
        this.data.clear();
    }
}
