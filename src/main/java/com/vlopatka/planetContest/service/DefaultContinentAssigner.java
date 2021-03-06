package com.vlopatka.planetContest.service;

import com.vlopatka.planetContest.domain.Continent;
import com.vlopatka.planetContest.domain.Planet;
import com.vlopatka.planetContest.event.Event;
import com.vlopatka.planetContest.service.dto.ContestDto;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

import static com.google.common.collect.Sets.newHashSet;

public class DefaultContinentAssigner implements ContinentAssigner {

    private final ConcurrentMap<Long, ContestDto> userToContestStore = new ConcurrentHashMap<>();
    private final CopyOnWriteArraySet<Consumer<Event>> consumers = new CopyOnWriteArraySet<>();

    private final PlanetInfoProvider planetInfoProvider;

    public DefaultContinentAssigner(PlanetInfoProvider planetInfoProvider) {
        this.planetInfoProvider = planetInfoProvider;
    }

    @Override
    public void assignContinent(long userId, long continentId) {
        userToContestStore.compute(userId, (k, v) -> processAssign(k, continentId, v));
    }

    private ContestDto processAssign(long userId, long continentId, ContestDto contestDtoOpt) {
        ContestDto contestDto = Optional.ofNullable(contestDtoOpt).orElse(initContest());
        return (contestDto.isFinished) ? contestDto : addContinent(userId, getContinentById(continentId), contestDto);
    }

    private ContestDto addContinent(long userId, Continent continent, ContestDto contestDto) {

        contestDto.planet.continents.add(continent);

        if (isContestFinished(contestDto.planet.continents)) {
            contestDto.isFinished = true;
            notifyAllConsumers(new Event(userId, Event.Type.CONTEST_FINISHED));
        }

        return contestDto;
    }

    private boolean isContestFinished(Set<Continent> userContinents) {
        return userContinents.equals(planetInfoProvider.get().continents);
    }

    private ContestDto initContest() {
        return new ContestDto(new Planet(1L, "Earth", newHashSet()), false);
    }

    private void notifyAllConsumers(Event event) {
        consumers.forEach(consumer -> consumer.accept(event));
    }

    private Continent getContinentById(long continentId) {
        final Optional<Continent> optCard = planetInfoProvider.get().continents.stream().filter(continent -> continent.id == continentId).findFirst();
        return optCard.orElseThrow(() -> new IllegalArgumentException("Non existed continentId: " + continentId));
    }

    @Override
    public void subscribe(Consumer<Event> consumer) {
        consumers.add(consumer);
    }
}
