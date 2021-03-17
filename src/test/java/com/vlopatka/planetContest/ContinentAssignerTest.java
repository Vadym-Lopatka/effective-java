package com.vlopatka.planetContest;

import com.vlopatka.planetContest.domain.Continent;
import com.vlopatka.planetContest.domain.Planet;
import com.vlopatka.planetContest.event.Event;
import com.vlopatka.planetContest.service.ContinentAssigner;
import com.vlopatka.planetContest.service.PlanetInfoProvider;
import com.vlopatka.planetContest.service.DefaultContinentAssigner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;
import static com.vlopatka.planetContest.event.Event.Type.CONTEST_FINISHED;
import static java.util.stream.Collectors.toList;
import static java.util.stream.LongStream.range;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContinentAssignerTest {

    @Mock
    private PlanetInfoProvider planetInfoProvider;

    private ContinentAssigner continentAssigner;

    @Before
    public void configure() {
        when(planetInfoProvider.get()).thenReturn(
                new Planet(3L, "Earth", newHashSet(
                        new Continent(1L, "Africa"),
                        new Continent(2L, "Asia"),
                        new Continent(3L, "Antarctica"),
                        new Continent(4L, "Europa"),
                        new Continent(5L, "North America"),
                        new Continent(6L, "South America"),
                        new Continent(7L, "Oceania/Australia")
                )));

        continentAssigner = new DefaultContinentAssigner(planetInfoProvider);
    }

    //<editor-fold defaultstate="collapsed" desc="pre-made acceptance scenarios">
    @Test
    public void assignAllContinentsTo10000UsersOnce() throws InterruptedException {
        assignAllContinentsTo10000Users(1);
    }

    @Test
    public void assignAllContinentsTo10000UsersTwice() throws InterruptedException {
        assignAllContinentsTo10000Users(2);
    }

    private void assignAllContinentsTo10000Users(int times) throws InterruptedException {
        // given
        List<Long> userIds = generateUserIds(10000L);
        Planet planet = planetInfoProvider.get();
        Collection<Event> expectedEvents = generateExpectedEvents(userIds);
        Collection<Event> actualEvents = subscribeEventListener(continentAssigner);

        // when
        for (int i = 0; i < times; i++) {
            assignAllcontinentsToUsersOnceConcurrently(continentAssigner, userIds, planet);
        }

        // then
        assertEquals("Expected " + expectedEvents.size() + " events, got " + actualEvents.size(), expectedEvents.size(), actualEvents.size());
    }

    private List<Long> generateUserIds(long size) {
        return range(0L, size)
                .boxed()
                .collect(toList());
    }

    /**
     * Generate output expected to be caught by {@link #subscribeEventListener(ContinentAssigner) event listener},
     * when each continent from {@code planet}'s {@code continents} is assigned to each user from {@code userIds} once.
     *
     * @return collection of expected events; for X users and Y continents in planet,
     * this will contain X {@code Event.Type.CONTEST_FINISHED}
     * @see Event.Type
     */
    private Collection<Event> generateExpectedEvents(Collection<Long> userIds) {
        return userIds
                .stream()
                .map(userId -> new Event(userId, CONTEST_FINISHED))
                .collect(Collectors.toList());
    }

    /**
     * Subscribes a listener to {@code continentAssigner} that counts events it receives. The listener is thread-safe.
     *
     * @return listener's live storage, a mapping of received events to number of times this event is received
     */
    private Collection<Event> subscribeEventListener(ContinentAssigner continentAssigner) {
        Collection<Event> container = new ConcurrentLinkedDeque<>();
        continentAssigner.subscribe(container::add);
        return container;
    }

    /**
     * Assigns all continents from {@code planet} to each user from {@code userIds} once concurrently, and waits for execution to finish
     */
    private void assignAllcontinentsToUsersOnceConcurrently(ContinentAssigner continentAssigner, Collection<Long> userIds, Planet planet) throws InterruptedException {
        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(10);
        threadPoolExecutor.prestartAllCoreThreads();
        CountDownLatch readySteadyGo = new CountDownLatch(1);

        userIds.parallelStream().forEach(
                userId -> planet.continents.parallelStream().forEach(
                        continent -> threadPoolExecutor.submit(
                                (Callable<Void>) () -> {
                                    readySteadyGo.await();
                                    continentAssigner.assignContinent(userId, continent.id);
                                    return null;
                                }
                        )
                )
        );

        readySteadyGo.countDown();

        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(20L, TimeUnit.SECONDS);
    }
    //</editor-fold>

}
