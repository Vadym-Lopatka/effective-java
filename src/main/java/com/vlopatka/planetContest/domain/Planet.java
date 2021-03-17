package com.vlopatka.planetContest.domain;

import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public class Planet {

    public final long id;
    public final String name;
    public final Set<Continent> continents;

    public Planet(long id, String name, Set<Continent> continents) {
        this.id = id;
        this.name = name;
        this.continents = continents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planet planet = (Planet) o;
        return id == planet.id &&
                Objects.equals(name, planet.name) &&
                Objects.equals(continents, planet.continents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, continents);
    }

    @Override
    public String toString() {
        return reflectionToString(this, SHORT_PREFIX_STYLE);
    }
}
