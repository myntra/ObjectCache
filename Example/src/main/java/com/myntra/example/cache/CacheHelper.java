package com.myntra.example.cache;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.myntra.objectcache.Entry;
import com.myntra.objectcache.ObjectCache;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okio.ByteString;

/**
 * @author mario
 */
public class CacheHelper {
    private ObjectCache objectCache;
    private MemCache memCache;
    private File cacheDir;
    private String folder;
    private static final int appVersion = 1;
    private static final String TAG = CacheHelper.class.getSimpleName();

    private CacheHelper() {}
    public static CacheHelper defaultCache(Context context) {
        CacheHelper cacheHelper = new CacheHelper();
        cacheHelper.cacheDir = context.getCacheDir();
        cacheHelper.memCache = MemCache.getInstance();
        return cacheHelper;
    }

    public static CacheHelper cacheInstance(Context context, String folder) {
        CacheHelper cacheHelper = new CacheHelper();
        cacheHelper.cacheDir = context.getCacheDir();
        cacheHelper.folder = folder;
        cacheHelper.memCache = MemCache.getInstance();
        return cacheHelper;
    }

    public void write(final String key, final JsonObject object, final long expiry) {
        final String hash = getHashedKey(key);
        try {
            memCache.write(hash, object, expiry);
            getObjectCache().write(hash, object, expiry);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage() ,e);
        }
    }

    public JsonObject fetch(String key) {
        String hash = getHashedKey(key);
        JsonObject object = null;
        try {
            Entry entry = memCache.fetch(hash);
            if (entry != null) {
                if (entry.hasExpired()) {
                    return null;
                }
                return (JsonObject) entry.value;
            }
            object = getObjectCache().fetch(hash, JsonObject.class);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage() ,e);
        }
        return object;
    }

    public JsonObject fetchEvenIfExpired(String key) {
        String hash = getHashedKey(key);
        JsonObject object = null;
        try {
            Entry entry = memCache.fetch(hash);
            if (entry != null) {
                return (JsonObject) entry.value;
            }
            object = getObjectCache().fetchEvenIfExpired(hash, JsonObject.class);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage() ,e);
        }
        return object;
    }

    public boolean requiresFetch(String key) {
        String hash = getHashedKey(key);
        boolean requiresFetch = true;
        try {
            Entry entry = memCache.fetch(hash);
            if (entry != null) {
                return entry.hasExpired();
            }
            requiresFetch = getObjectCache().hasExpired(hash) == null ? true : objectCache.hasExpired(hash);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage() ,e);
        }
        return requiresFetch;
    }

    public boolean remove(String key) {
        String hash = getHashedKey(key);
        boolean remove = false;
        try {
            memCache.remove(hash);
            remove = getObjectCache().remove(hash);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage() ,e);
        }
        return remove;
    }

    private ObjectCache getObjectCache() {
        if (this.objectCache == null) {
            if (StringUtils.isNotEmpty(this.folder)) {
                this.objectCache = ObjectCache.cacheInstance(cacheDir, folder, appVersion);
                return this.objectCache;
            }
            this.objectCache = ObjectCache.defaultCache(cacheDir, appVersion);
        }
        return this.objectCache;
    }

    private String getHashedKey(String input) {
        String key = input;
        try {
            key = shaBase64(input);
        }
        catch (Exception e) {
            // Do Nothing
        }
        return key;
    }

    /** Returns a Base 64-encoded string containing a SHA-1 hash of {@code s}. */
    public static String shaBase64(String s) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] sha1Bytes = messageDigest.digest(s.getBytes("UTF-8"));
            return ByteString.of(sha1Bytes).hex();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
