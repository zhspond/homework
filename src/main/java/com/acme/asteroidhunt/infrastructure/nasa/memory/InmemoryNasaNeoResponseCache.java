package com.acme.asteroidhunt.infrastructure.nasa.memory;

import com.acme.asteroidhunt.AsteroidhuntException;
import com.acme.asteroidhunt.domain.model.DateRange;
import com.acme.asteroidhunt.infrastructure.nasa.NasaNeoResponseCache;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/*
    TODO the cache is in-memory and has some obvious limitations:
    - cache is not shared between multiple instances
    - data is lost at redeploys
    - increases memory needs of the service

    Alternatives for a more prod ready solution:
    - a persistent / semi persistent key value store
    - an idea to consider: doing all caching in an http proxy, then the app wouldn't even need to know about it
 */
@Service
public class InmemoryNasaNeoResponseCache implements NasaNeoResponseCache {
    private final Cache<DateRange, String> neosByDateRange;
    private final Cache<String, Optional<String>> neoDetailsById;

    public InmemoryNasaNeoResponseCache() {
        this.neosByDateRange = CacheBuilder.newBuilder().softValues().build();
        this.neoDetailsById = CacheBuilder.newBuilder().softValues().build();
    }

    @Override
    public String neosForRange(DateRange range, Supplier<String> supplier) {
        try {
            return neosByDateRange.get(range, supplier::get);
        } catch (ExecutionException e) {
            throw new AsteroidhuntException(e);
        }
    }

    public Optional<String> detailsOf(String id, Supplier<Optional<String>> supplier) {
        try {
            return neoDetailsById.get(id, supplier::get);
        } catch (ExecutionException e) {
            throw new AsteroidhuntException(e);
        }
    }
}
