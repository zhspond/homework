package com.acme.asteroidhunt.domain.model;

import java.util.List;

public record Neo(
        String id,
        String name,
        double diameterKilometers,
        List<Approach> approaches
) {}