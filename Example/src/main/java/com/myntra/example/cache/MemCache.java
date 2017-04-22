package com.myntra.example.cache;

import android.util.Log;
import android.util.LruCache;

import com.myntra.objectcache.Entry;

/**
 * @author mario
 */
public class MemCache {
    private LruCache<String, Entry> memCache;

    private static MemCache instance;

    private MemCache() {
        // Find out maximum memory available to application
        // 1024 is used because LruCache constructor takes int in kilobytes
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/16th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 16;
        Log.d(getClass().getName(), "max memory " + maxMemory + " cache size " + cacheSize);

        this.memCache = new LruCache<String, Entry>(cacheSize) {
            @Override
            protected int sizeOf(String key, Entry value) {
                return super.sizeOf(key, value);
            }
        };
    }

    public static MemCache getInstance() {
        if (instance == null) {
            instance = new MemCache();
        }
        return instance;
    }

    public void write(String key, Object object, long expiry) {
        memCache.put(key, new Entry(object, expiry));
    }

    public Entry fetch(String key) {
        return memCache.get(key);
    }

    public Object fetchEvenIfExpired(String key) {
        return memCache.get(key);
    }

    public void remove(String key) {
        memCache.remove(key);
    }
}
