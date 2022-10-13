package com.acme.asteroidhunt.domain.model;

public record NeoCharacteristics(
        String id,
        String name,
        double absoluteMagnitudeH,
        String designation,
        double diamaterMinKilometers,
        double diameterMaxKilometers
) {}
