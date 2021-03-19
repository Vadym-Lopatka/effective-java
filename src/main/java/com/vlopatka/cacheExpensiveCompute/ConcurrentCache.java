package com.vlopatka.cacheExpensiveCompute;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ConcurrentCache<K, V> implements Cache<K, V> {
    private final ConcurrentMap<K, Future<V>> store = new ConcurrentHashMap<>();
    private final Computable<K, V> computable;

    public ConcurrentCache(Computable computable) {
        this.computable = computable;
    }

    @Override
    public V get(K key) {
        while (true) {
            Future<V> future = store.get(key);

            if (future == null) {
                final FutureTask<V> futureTask = new FutureTask<V>(() -> computable.expensiveFunction(key));

                future = store.putIfAbsent(key, futureTask);
                if (future == null) {
                    future = futureTask;
                    futureTask.run();
                }
            }

            try {
                return future.get();
            } catch (CancellationException e) {
                store.remove(key, future);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void evict(K key) {
        store.remove(key);
    }

    @Override
    public void evictAll() {
        store.clear();
    }

    @Override
    public int size() {
        return store.size();
    }
}
