package com.acme.asteroidhunt.test;

import com.acme.asteroidhunt.domain.model.DateRange;
import com.acme.asteroidhunt.infrastructure.nasa.NasaNeoFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Optional;

@Configuration
public class TestConfig {
    // TODO this is an overly dumb mock for now
    @Primary
    @Bean
    public NasaNeoFetcher mockFetcher() {
        return new NasaNeoFetcher() {
            @Override
            public String fetchNeos(DateRange range) {
                return ClasspathReader.asString("nasaneosresponse.json");
            }

            @Override
            public Optional<String> fetchDetailsOfNeo(String id) {
                return Optional.of(ClasspathReader.asString("nasadetailsresponse.json"));
            }
        };
    }
}
