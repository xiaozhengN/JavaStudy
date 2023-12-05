package cache.impl;

import cache.Cache;
import cache.CacheDataLoader;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * LRU缓存
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-11-29 23:10:09
 */
public class LRUCache<K, V> implements Cache<K, V> {
    private final int size;

    private final CacheDataLoader<K, V> loader;

    private final Map<K, V> data = new ConcurrentHashMap<>();

    private final ConcurrentLinkedDeque<K> queue = new ConcurrentLinkedDeque<>();

    LRUCache(CacheDataLoader<K, V> loader, int size) {
        if (loader == null) {
            throw new IllegalArgumentException("Null CacheDataLoader");
        }

        this.loader = loader;
        this.size = size;
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
        } else {
            if (queue.peekLast() != key) {
                this.queue.removeLastOccurrence(key);
                this.queue.add(key);
            }
        }

        return value;
    }

    @Override
    public void getAll(Collection<K> keys, Map<K, V> result) {
        ArrayList<K> missingKeyList = new ArrayList<>();

        for (K key : keys) {
            V value = this.data.get(key);
            if (value != null) {
                result.put(key, value);
                this.queue.removeLastOccurrence(key);
                this.queue.add(key);
            } else {
                missingKeyList.add(key);
            }
        }

        if (missingKeyList.isEmpty()) {
            return;
        }

        HashMap<K, V> loadedMap = new HashMap<>();
        this.loader.loadAll(missingKeyList, loadedMap);
        result.putAll(loadedMap);
        putAll(loadedMap);
    }

    @Override
    public void put(K key, V value) {
        if (key == null || value == null) {
            return;
        }

        if (this.data.put(key, value) != null) {
            if (this.queue.peekLast() != key) {
                this.queue.removeLastOccurrence(key);
                this.queue.add(key);
            }
        } else {
            this.queue.add(key);
        }

        if (this.data.size() <= this.size) {
            return;
        }

        K oldestKey = this.queue.poll();
        this.data.remove(oldestKey);
    }

    @Override
    public void putAll(Map<K, V> map) {
        if (map == null || map.size() == 0) {
            return;
        }
        map.forEach(this::put);
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        this.queue.remove(key);
        return this.data.remove(key);
    }

    @Override
    public Collection<V> removeAll(Collection<K> keys) {
        ArrayList<V> removedList = new ArrayList<>(keys.size());
        for (K key : keys) {
            V value = remove(key);
            if (value != null) {
                removedList.add(value);
            }
        }
        return removedList;
    }

    @Override
    public void clear() {
        this.data.clear();
        this.queue.clear();
    }

    @Override
    public Map<K, V> getAll() {
        return Collections.unmodifiableMap(data);
    }
}
