package com.acme.asteroidhunt.infrastructure.nasa;

import com.acme.asteroidhunt.domain.model.DateRange;

import java.util.Optional;
import java.util.function.Supplier;

public interface NasaNeoResponseCache {
    String neosForRange(DateRange range, Supplier<String> supplier);
    Optional<String> detailsOf(String id, Supplier<Optional<String>> supplier);
}
