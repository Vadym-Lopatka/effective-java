package com.vlopatka.planetContest;

import java.util.Arrays;
import java.util.Stack;

public class AsteroidCollision {

    public static void main(String[] args) {
        var a = new AsteroidCollision();
        System.out.println(Arrays.toString(a.asteroidCollision(new int[]{10, 2, -5})));
    }

    public int[] asteroidCollision(int[] asteroids) {
        Stack<Integer> stack = new Stack<>();

        int i = 0;
        while (i < asteroids.length) {
            int asteroid = asteroids[i];
            if (asteroid > 0) {
                stack.push(asteroid);
            } else {
                doCollision(stack, asteroid);

                if (stack.isEmpty() || stack.peek() < 0) {
                    stack.push(asteroid);
                } else if (stack.peek() == Math.abs(asteroid)) {
                    stack.pop();
                }
            }
            i++;
        }

        return mapToResult(stack);
    }

    private void doCollision(Stack<Integer> stack, int asteroid) {
        while (!stack.isEmpty() && stack.peek() > 0 && stack.peek() < Math.abs(asteroid)) {
            stack.pop();
        }
    }

    private int[] mapToResult(Stack<Integer> stack) {
        int[] result = new int[stack.size()];
        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = stack.pop();
        }

        return result;
    }
}
