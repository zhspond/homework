package com.acme.asteroidhunt.infrastructure.nasa;

import com.acme.asteroidhunt.domain.model.DateRange;

import java.util.Optional;

public interface NasaNeoFetcher {
    String fetchNeos(DateRange range);
    Optional<String> fetchDetailsOfNeo(String id);
}
