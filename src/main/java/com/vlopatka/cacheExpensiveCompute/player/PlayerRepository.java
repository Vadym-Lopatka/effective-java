package com.vlopatka.cacheExpensiveCompute.player;

import com.vlopatka.cacheExpensiveCompute.Computable;

public class PlayerRepository implements Computable<Long,Player> {

    public Player findById(Long id) {
        return new Player(id);
    }

    @Override
    public Player expensiveFunction(Long key) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return findById(key);
    }
}
