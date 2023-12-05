package cache.impl;

import cache.Cache;
import cache.CacheDataLoader;
import cache.CacheDataStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-11-29 23:24:54
 */
public class TwoLevelCache<K, V> implements Cache<K, V> {
    private final Cache<K, V> storeCache;

    private final int memoryCacheSize;

    private Map<K, V> memoryCache = new ConcurrentHashMap<>();

    private ConcurrentLinkedDeque<K> queue = new ConcurrentLinkedDeque<>();

    TwoLevelCache(CacheDataStore<K, V> store, CacheDataLoader<K, V> loader, int memoryCacheSize) {
        if (store == null) {
            throw new IllegalArgumentException("Null CaheDataStore");
        }

        if (loader == null) {
            throw new IllegalArgumentException("Null CacheDataLoader");
        }

        this.memoryCacheSize = memoryCacheSize;
        this.storeCache = new StoreCache<>(store, loader);
    }


    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }

        V value = this.memoryCache.get(key);

        if (value == null) {
            value = this.storeCache.get(key);
            putData(key, value, false);
        } else {
            if (queue.peekLast() != key) {
                this.queue.remove(key);
                this.queue.add(key);
            }
        }

        return value;
    }

    @Override
    public void getAll(Collection<K> keys, Map<K, V> result) {
        ArrayList<K> missingKeys = new ArrayList<>();

        for (K key : keys) {
            V value = this.memoryCache.get(key);
            if (value != null) {
                result.put(key, value);
                this.queue.remove(key);
                this.queue.add(key);
            } else {
                missingKeys.add(key);
            }
        }

        if (missingKeys.isEmpty()) {
            return;
        }

        HashMap<K, V> loaded = new HashMap<>();
        this.storeCache.getAll(missingKeys, loaded);
        result.putAll(loaded);
        putAllData(loaded, false);
    }

    @Override
    public void put(K key, V value) {
        putData(key, value, true);
    }

    @Override
    public void putAll(Map<K, V> map) {
        putAllData(map, true);
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            return null;
        }

        this.queue.remove(key);
        V memoryValue = this.memoryCache.remove(key);

        if (memoryValue != null) {
            return memoryValue;
        }

        return this.storeCache.remove(key);
    }

    @Override
    public Collection<V> removeAll(Collection<K> keys) {
        for (K key : keys) {
            this.queue.remove(key);
            this.memoryCache.remove(key);
        }

        return new ArrayList<>(this.storeCache.removeAll(keys));
    }

    @Override
    public void clear() {
        this.queue.clear();
        this.memoryCache.clear();
        this.storeCache.clear();
    }

    private void putData(K key, V value, boolean saveStore) {
        if (key == null || value == null) {
            return;
        }

        this.memoryCache.put(key, value);
        this.queue.add(key);

        if (saveStore) {
            this.storeCache.put(key, value);
        }

        if (this.memoryCache.size() <= this.memoryCacheSize) {
            return;
        }

        K oldestKey = this.queue.poll();
        this.memoryCache.remove(oldestKey);
    }

    private void putAllData(Map<K, V> map, boolean saveStore) {
        if (map == null || map.size() == 0) {
            return;
        }

        this.memoryCache.putAll(map);
        this.queue.addAll(map.keySet());

        while (this.memoryCache.size() > this.memoryCacheSize) {
            K oldestKey = this.queue.poll();
            this.memoryCache.remove(oldestKey);
        }

        if (!saveStore) {
            return;
        }

        this.storeCache.putAll(map);
    }
}
