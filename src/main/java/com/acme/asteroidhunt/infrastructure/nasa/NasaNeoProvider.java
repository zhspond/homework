package com.acme.asteroidhunt.infrastructure.nasa;

import com.acme.asteroidhunt.domain.infrastructure.NeoProvider;
import com.acme.asteroidhunt.domain.model.DateRange;
import com.acme.asteroidhunt.domain.model.Neo;
import com.acme.asteroidhunt.domain.model.NeoCharacteristics;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Provides NEO provider using Nasa API.
 * Has a layer of caching to reduce API usage.
 */
@Service
public class NasaNeoProvider implements NeoProvider {
    private final NasaNeoResponseCache responseCache;
    private final NasaNeoFetcher fetcher;
    private final NasaNeoParser parser;

    public NasaNeoProvider(NasaNeoResponseCache responseCache, NasaNeoFetcher fetcher, NasaNeoParser parser) {
        this.responseCache = responseCache;
        this.fetcher = fetcher;
        this.parser = parser;
    }

    @Override
    public List<Neo> forRange(DateRange range) {
        String response = responseCache.neosForRange(
                range,
                () -> fetcher.fetchNeos(range)
        );
        return parser.parseNeos(response);

    }

    @Override
    public Optional<NeoCharacteristics> detailsOf(String id) {
        Optional<String> response = responseCache.detailsOf(
                id,
                () -> fetcher.fetchDetailsOfNeo(id)
        );
        return response.map(parser::parseCharacteristics);
    }
}
