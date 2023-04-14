package com.myntra.objectcache;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jakewharton.disklrucache.DiskLruCache;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.myntra.objectcache.BuildConfig;

/**
 * @author mario
 */
public class ObjectCache {
    static Gson gson = new Gson();
    DiskLruCache cache;
    static final long SIZE = 10 * 1024 * 1024;
    static final String DEFAULT_CACHE_DIR = "com.myntra.objectcache";
    static final String STRING_KEY_PATTERN = "[a-z0-9_-]{1,120}";
    public static final Pattern LEGAL_KEY_PATTERN = Pattern.compile(STRING_KEY_PATTERN);

    public static ObjectCache defaultCache(File cacheDir, int appVersion) {
        return cacheInstance(cacheDir, DEFAULT_CACHE_DIR, appVersion);
    }

    public static ObjectCache cacheInstance(File cacheDir, String folder, int appVersion) {
        ObjectCache objectCache = new ObjectCache();
        File file = new File(cacheDir, folder);
        try {
            objectCache.cache = DiskLruCache.open(file, appVersion, 1, SIZE);
        } catch (IOException e) {
            if(BuildConfig.DEBUG)
                e.printStackTrace();
        }
        return objectCache;
    }

    public static ObjectCache cacheInstance(File cacheDir, String folder, int appVersion, long size) {
        ObjectCache objectCache = new ObjectCache();
        File file = new File(cacheDir, folder);
        try {
            objectCache.cache = DiskLruCache.open(file, appVersion, 1, size);
        } catch (IOException e) {
            if(BuildConfig.DEBUG)
                e.printStackTrace();
        }
        return objectCache;
    }

    public void write(String key, Object object, long expiry) {
        key = sanitizeKey(key);
        try {
            Entry entry = new Entry(object, expiry);
            cache.remove(key);
            DiskLruCache.Editor editor = cache.edit(key);
            IOUtils.write(new Gson().toJson(entry), editor.newOutputStream(0));
            cache.flush();
            editor.commit();
        } catch (IOException e) {
            if(BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    public <T> T fetch(String key, Class<T> classOfT) {
        key = sanitizeKey(key);
        try {
            DiskLruCache.Snapshot snapshot = cache.get(key);
            if (snapshot != null) {
                String data = snapshot.getString(0);
                JsonObject entryJson = gson.fromJson(data, JsonObject.class);
                Entry entry = gson.fromJson(entryJson, Entry.class);
                if (!entry.hasExpired()) {
                    JsonElement element = entryJson.get(Entry.VALUE);
                    if (element.isJsonPrimitive()) {
                        return gson.fromJson(element.getAsJsonPrimitive(), classOfT);
                    }
                    return gson.fromJson(element.getAsJsonObject(), classOfT);
                }
            }
        } catch (Exception e) {
            if(BuildConfig.DEBUG)
                e.printStackTrace();
        }
        return null;
    }

    public <T> T fetchEvenIfExpired(String key, Class<T> classOfT) {
        key = sanitizeKey(key);
        try {
            DiskLruCache.Snapshot snapshot = cache.get(key);
            if (snapshot != null) {
                String data = snapshot.getString(0);
                JsonObject entryJson = gson.fromJson(data, JsonObject.class);
                return gson.fromJson(entryJson.getAsJsonObject(Entry.VALUE), classOfT);
            }
        } catch (IOException e) {
            if(BuildConfig.DEBUG)
                e.printStackTrace();
        }
        return null;
    }

    public Boolean hasExpired(String key) {
        key = sanitizeKey(key);
        try {
            DiskLruCache.Snapshot snapshot = cache.get(key);
            if (snapshot != null) {
                String data = snapshot.getString(0);
                Entry entry = gson.fromJson(data, Entry.class);
                return entry.hasExpired();
            }
        } catch (IOException e) {
            if(BuildConfig.DEBUG)
                e.printStackTrace();
        }
        return null;
    }

    public boolean remove(String key) {
        key = sanitizeKey(key);
        try {
            return cache.remove(key);
        } catch (IOException e) {
            if(BuildConfig.DEBUG)
                e.printStackTrace();
        }
        return false;
    }

    private String sanitizeKey(String key) {
        Matcher matcher = LEGAL_KEY_PATTERN.matcher(key);
        if (matcher.matches()) {
            return key;
        }
        key = key.toLowerCase().replaceAll("[^a-z0-9_-]", "-");
        return key;
    }
}