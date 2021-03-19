package com.vlopatka.cacheExpensiveCompute.player;

import com.vlopatka.cacheExpensiveCompute.Cache;
import com.vlopatka.cacheExpensiveCompute.ConcurrentCache;

public class PlayerService {
    private final Cache<Long,Player> playerCache;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerCache = new ConcurrentCache<Long,Player>(playerRepository);
    }

    public Player getPlayer(Long id) {
        return playerCache.get(id);
//        return playerRepository.findById(id);
    }
}
