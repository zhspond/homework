package com.acme.asteroidhunt.domain.infrastructure;

import com.acme.asteroidhunt.domain.model.DateRange;
import com.acme.asteroidhunt.domain.model.Neo;
import com.acme.asteroidhunt.domain.model.NeoCharacteristics;

import java.util.List;
import java.util.Optional;

/**
 * Infrastructure agnostic provider of neo information for the domain to use
 */
public interface NeoProvider {
    List<Neo> forRange(DateRange range);
    Optional<NeoCharacteristics> detailsOf(String id);
}
