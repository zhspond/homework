package com.acme.asteroidhunt.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NeoSorterTest {
    private final NeoSorter neoSorter = new NeoSorter();

    @Test
    void neos_sorted_by_diameter() {
        var testSet = List.of(
                neo("1", 34.0, 99.0),
                neo("2", 33.0, 199.0),
                neo("3", 99.0, 233.0),
                neo("4", 284.0, 125.0),
                neo("5", 12.0, 30.4)
        );

        List<Neo> largest = neoSorter.largest(testSet, 3);

        assertThat(largest.stream().map(Neo::id).toList()).containsExactlyInAnyOrder("1", "3", "4");
    }

    @Test
    void neos_sorted_by_closest_approach() {
        var testSet = List.of(
                neo("1", 12.0, 15.0),
                neo("2", 12.0, 23.0),
                neo("3", 12.0, 140.0),
                neo("4", 12.0, 93.0),
                neo("5", 12.0, 64.0)
        );

        List<Neo> closest = neoSorter.closest(testSet, 3);

        assertThat(closest.stream().map(Neo::id).toList()).containsExactlyInAnyOrder("1", "2", "5");
    }

    private Neo neo(String id, double diameter, double closestApproach) {
        return new Neo(id, id, diameter, List.of(new Approach(closestApproach), new Approach(closestApproach + 10.0)));
    }
}
