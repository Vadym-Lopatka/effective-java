package com.vlopatka.planetContest;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MaxStack {

    private final PriorityQueue<Integer> store;

    public MaxStack() {
       store = new PriorityQueue<>(Comparator.reverseOrder());
    }

    public void push(int x) {
        store.add(x);
    }

    public int pop() {
        return store.poll();
    }

    public int top() {
        return store.peek();
    }

    public int peekMax() {
        return store.poll();
    }

    public int popMax() {
        return store.poll();
    }
}
