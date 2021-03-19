package com.vlopatka.cacheExpensiveCompute;

public interface Computable<K,V> {
    public abstract V expensiveFunction(K key);
}
