package com.myntra.objectcache;

import java.util.Objects;

/**
 * @author mario
 */
public class Entry {
    public static final String VALUE = "value";
    public long createdAt;
    public long expiry;
    public Object value;

    public Entry(Object value, long expiry) {
        this.value = value;
        this.expiry = expiry;
        this.createdAt = System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() > (this.createdAt + this.expiry);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Entry{");
        sb.append("createdAt=").append(createdAt);
        sb.append(", expiry=").append(expiry);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return createdAt == entry.createdAt &&
                expiry == entry.expiry &&
                Objects.equals(value, entry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, expiry, value);
    }
}
