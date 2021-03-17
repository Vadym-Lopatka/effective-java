package com.vlopatka.planetContest.service;

import com.vlopatka.planetContest.event.Event;

import java.util.function.Consumer;

public interface ContinentAssigner {

    void assignContinent(long userId, long continentId);

    void subscribe(Consumer<Event> consumer);
}
