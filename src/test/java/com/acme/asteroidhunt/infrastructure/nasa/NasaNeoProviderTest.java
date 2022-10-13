package com.acme.asteroidhunt.infrastructure.nasa;

import com.acme.asteroidhunt.domain.model.DateRange;
import com.acme.asteroidhunt.domain.model.Neo;
import com.acme.asteroidhunt.infrastructure.nasa.memory.InmemoryNasaNeoResponseCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NasaNeoProviderTest {
    private final NasaNeoResponseCache responseCache = new InmemoryNasaNeoResponseCache();
    private final NasaNeoFetcher fetcher = mock(NasaNeoFetcher.class);
    private final NasaNeoParser parser = mock(NasaNeoParser.class);

    private final NasaNeoProvider nasaNeoProvider = new NasaNeoProvider(
            responseCache, fetcher, parser
    );

    @BeforeEach
    void init() {
        when(parser.parseNeos(any(String.class)))
                .thenReturn(List.of(new Neo("1", "foo", 3.5, List.of())));
        when(fetcher.fetchNeos(any(DateRange.class))).thenReturn("");
    }

    @Test
    void provider_returns_neos_using_fetcher() {
        var neos = nasaNeoProvider.forRange(new DateRange(LocalDate.now(), LocalDate.now()));
        assertThat(neos).hasSize(1);
        assertThat(neos.get(0).name()).isEqualTo("foo");
    }

    @Test
    void neo_queries_are_cached_and_performed_only_once() {
        nasaNeoProvider.forRange(new DateRange(LocalDate.now(), LocalDate.now()));
        nasaNeoProvider.forRange(new DateRange(LocalDate.now(), LocalDate.now()));

        verify(fetcher, times(1)).fetchNeos(any(DateRange.class));
    }

    @Test
    void details_queries_are_cached_and_performed_only_once() {
        when(fetcher.fetchDetailsOfNeo("1")).thenReturn(Optional.of("foo"));
        when(fetcher.fetchDetailsOfNeo("2")).thenReturn(Optional.of("foo"));

        nasaNeoProvider.detailsOf("1");
        nasaNeoProvider.detailsOf("2");
        nasaNeoProvider.detailsOf("1");

        verify(fetcher, times(2)).fetchDetailsOfNeo(any(String.class));
        verify(fetcher, times(1)).fetchDetailsOfNeo("1");
        verify(fetcher, times(1)).fetchDetailsOfNeo("2");
    }
}
