package com.vlopatka.planetContest.service.dto;

import com.vlopatka.planetContest.domain.Planet;

public class ContestDto {

    public Planet planet;
    public boolean isFinished;

    public ContestDto(Planet planet, boolean isFinished) {
        this.planet = planet;
        this.isFinished = isFinished;
    }

}
