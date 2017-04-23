package com.myntra.objectcache;

import java.util.concurrent.TimeUnit;

/**
 * @author mario
 * A small helper class to set expiry for the cache
 */
public class Expiry {
    public static long MINUTES(long minutes) {
        return TimeUnit.MINUTES.toMillis(minutes);
    }
    public static long SECONDS(long seconds) {
        return TimeUnit.SECONDS.toMillis(seconds);
    }
    public static long HOURS(long hours) {
        return TimeUnit.HOURS.toMillis(hours);
    }
    public static long DAYS(long days) {
        return TimeUnit.DAYS.toMillis(days);
    }
}
