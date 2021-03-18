# Task

Implement a service that describes a contest.
The goal is to collect a set of continents that belongs to a planet.

## Domain model

System has configuration that consists of one Planet and set of Continents. Planet contains Continents.
Example:

Planet "Earth"

    - Continent "Africa"
    - Continent "Asia"
    - Continent "Europa"

## Functional requirements

Service has to support the following functions:

- add a single continent to a user's collection
  A continent can be assigned to the user only once: additional copies of the same continent have no effect on the user's collection.

- send an event to external systems when the user has collected all continents of the planet

  The event should be sent only once.

- allow several external systems to subscribe to the service to receive events
  Once a system subscribes, it will receive all subsequent events. It should not receive events generated before it subscribed.

## Technical aspects

The configuration is supplied at runtime via an instance of *PlanetInfoProvider* interface. Implementing this interface is not required.

Domain objects of the same type (i.e. all Planet and all Continents) have unique ids.

The state of each user's continent collection should be stored in memory.

Requests for adding continents will be called in a multithreaded environment.


*ContinentAssignerTest* already contains some end-to-end scenarios made to verify the code.
