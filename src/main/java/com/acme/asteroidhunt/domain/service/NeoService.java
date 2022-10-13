package com.acme.asteroidhunt.domain.service;

import com.acme.asteroidhunt.domain.infrastructure.NeoProvider;
import com.acme.asteroidhunt.domain.model.DateRange;
import com.acme.asteroidhunt.domain.model.Neo;
import com.acme.asteroidhunt.domain.model.NeoCharacteristics;
import com.acme.asteroidhunt.domain.model.NeoSorter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
  // TODO preferably split this service to multiple single purpose services
 */
@Service
public class NeoService {
    private static final int CLOSEST_SHOW_COUNT = 10;

    private final NeoProvider provider;
    private final NeoSorter sorter;

    public NeoService(
            NeoProvider provider,
            NeoSorter sorter
    ) {
        this.provider = provider;
        this.sorter = sorter;
    }

    public List<Neo> findClosestNeos(DateRange range) {
        return sorter.closest(
                provider.forRange(range),
                CLOSEST_SHOW_COUNT
        );
    }

    public Optional<NeoCharacteristics> findCharacteristicsOfLargestNeo(DateRange range) {
        List<Neo> largestObjects = sorter.largest(
                provider.forRange(range),
                1
        );
        Optional<Neo> largestObject = !largestObjects.isEmpty() ?
                Optional.of(largestObjects.get(0)) :
                Optional.empty();

        return largestObject.flatMap(
                o -> provider.detailsOf(o.id())
        );
    }
}
