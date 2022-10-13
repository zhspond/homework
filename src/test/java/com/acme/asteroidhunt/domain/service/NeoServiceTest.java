package com.acme.asteroidhunt.domain.service;

import com.acme.asteroidhunt.domain.model.DateRange;
import com.acme.asteroidhunt.domain.model.Neo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/*
 TODO consider doing this high level test through the API to also cover that layer
 time constraint, so kept as it is
*/
@SpringBootTest
class NeoServiceTest {
    @Autowired
    private NeoService neoService;

    @Test
    void closest_asteroids_are_found() {
        var neos = neoService.findClosestNeos(new DateRange(LocalDate.now(), LocalDate.now()));
        assertThat(neos).hasSize(10);
        assertThat(neos.stream().map(Neo::id).toList())
                .containsExactly(
                        "3726788",
                        "3726710",
                        "3727181",
                        "3727036",
                        "3727663",
                        "3730577",
                        "3727179",
                        "3731587",
                        "3759690",
                        "3727639"
                );
    }

    @Test
    void largest_asteroid_characteristics_returned() {
        var largest = neoService.findCharacteristicsOfLargestNeo(new DateRange(LocalDate.now(), LocalDate.now()));
        assertThat(largest).isPresent();
        assertThat(largest.get().id()).isEqualTo("3542519");
        // TODO more assertions on correctness
    }

}
