package cache.impl;

import cache.Cache;
import cache.CacheDataLoader;
import cache.CacheDataStore;
import exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * StoreCache
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-05 23:01:41
 */
@Slf4j
public class StoreCache<K, V> implements Cache<K, V> {
    private final CacheDataLoader<K, V> loader;

    private final CacheDataStore<K, V> store;

    private boolean storeReady;

    StoreCache(CacheDataStore<K, V> store, CacheDataLoader<K, V> loader) {
        if (store == null) {
            throw new IllegalArgumentException("Null CacheDataStore!");
        }
        if (loader == null) {
            throw new IllegalArgumentException("Null CacheDataLoader!");
        }
        this.storeReady = false;
        this.store = store;
        this.loader = loader;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }

        V value = null;

        if (this.storeReady) {
            try {
                value = this.store.load(key);
            } catch (Exception e) {
                log.error("store.load: " + key, e);
            }
        }

        if (value == null) {
            value = this.loader.load(key);
            put(key, value);
        }

        return value;
    }

    @Override
    public void getAll(Collection<K> keys, Map<K, V> result) {
        List<K> missingKeys = new ArrayList<>();

        try {
            if (this.storeReady) {
                this.store.loadAll(keys, result);
            }
        } catch (Exception e) {
            log.error("", e);
        }

        for (K key : keys) {
            if (!result.containsKey(key)) {
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

        if (!this.storeReady) {
            initStore();
        }

        try {
            this.store.save(key, value);
        } catch (Exception e) {
            log.error("store.save: " + key, e);
        }
    }

    private synchronized void initStore() {
        if (this.storeReady) {
            return;
        }

        try {
            this.store.init();
            this.storeReady = true;
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void putAll(Map<K, V> map) {
        if (map == null || map.size() == 0) {
            return;
        }

        if (!this.storeReady) {
            initStore();
        }

        try {
            this.store.saveAll(map);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public V remove(K key) {
        if (this.storeReady) {
            try {
                return this.store.remove(key);
            } catch (Exception e) {
                log.error("store.remove: " + key, e);
                return null;
            }
        }

        return null;
    }

    @Override
    public Collection<V> removeAll(Collection<K> keys) {
        if (this.storeReady) {
            try {
                Collection<V> removed = this.store.removeAll(keys);
                if (removed != null) {
                    return removed;
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void clear() {
        if (!this.storeReady) {
            return;
        }
        try {
            this.store.clear();
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
