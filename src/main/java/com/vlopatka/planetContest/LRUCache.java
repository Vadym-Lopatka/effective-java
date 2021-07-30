package com.vlopatka.planetContest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUCache {

    public static void main(String[] args) {
        //      ["LRUCache","get","put","get","put","put","get","get"]
        //      [   [2],     [2], [2,6], [1], [1,5],[1,2], [1],  [2]]
        // Output [null,    -1,    null, -1,   null, null,  2,   -1]
        // Expected [null,  -1,    null, -1,   null, null,  2,   6]


        LRUCache lRUCache = new LRUCache(2);
        lRUCache.get(2);
        lRUCache.put(2,6);
        lRUCache.get(1);
        lRUCache.put(1,5);
        lRUCache.put(1,2);
        lRUCache.get(1);
        System.out.println(lRUCache.get(2));
    }


    private final Map<Integer, Integer> store;
    private final int capacity;
    private final LinkedList<Integer> recentCalls;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        store = new HashMap<>(capacity);
        recentCalls = new LinkedList<>();
    }

    public int get(int key) {
        final Integer result = store.get(key);
        if (result != null) {
            updateRecent(key);
            return result;
        }

        return -1;
    }

    private void updateRecent(int key) {
        if (recentCalls.size() != capacity) {
            if (recentCalls.isEmpty()) {
                recentCalls.addFirst(key);
            } else {
                final Integer first = recentCalls.getFirst();
                if (!first.equals(key)) {
                    recentCalls.addFirst(key);
                }
            }
        } else {
            final Integer first = recentCalls.getFirst();
            if (!first.equals(key)) {
                recentCalls.removeLast();
                recentCalls.addFirst(key);
            }
        }
    }

    public void put(int key, int value) {
        if (store.size() == capacity && store.get(key) == null) {
            store.remove(recentCalls.getLast());
        }

        updateRecent(key);
        store.put(key, value);
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */