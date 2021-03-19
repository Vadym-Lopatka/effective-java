package com.vlopatka.cacheExpensiveCompute;

import com.vlopatka.cacheExpensiveCompute.player.Player;
import com.vlopatka.cacheExpensiveCompute.player.PlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConcurrentCacheTest {

    private final ConcurrentCache<Long, Player> cache = new ConcurrentCache<>(new PlayerRepository());

    @AfterEach
    public void cleanup() {
        cache.evictAll();
    }

    @Test
    public void shouldAddEntityToCacheOnGetCall() {
        // given
        long playerId = 1L;

        // when
        final Player player = cache.get(playerId);

        // then
        assertEquals(1, cache.size());
    }

    @Test
    public void shouldReturnValidEntityFromCache() {
        // given
        long playerId = 1L;

        // when
        final Player player = cache.get(playerId);

        // then
        assertEquals(playerId, player.getId());
    }

    @Test
    public void shouldEvictEntityInCache() {
        // given
        long playerId = 1L;
        final Player player = cache.get(playerId);
        int sizeBeforeEviction = cache.size();

        // when
        cache.evict(playerId);

        // then
        assertEquals(sizeBeforeEviction - 1, cache.size());
    }

    @Test
    public void shouldClearCacheStore() {
        // given
        cache.get(1L);
        cache.get(2L);

        // when
        cache.evictAll();

        // then
        assertEquals(0, cache.size());
    }

}
