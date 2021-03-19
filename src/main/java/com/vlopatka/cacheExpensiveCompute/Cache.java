package com.vlopatka.cacheExpensiveCompute;

public interface Cache<K,V> {
    public abstract V get(K key);
    public abstract void evict(K key);
    public abstract void evictAll();
    public abstract int size();
}
