package com.acme.asteroidhunt.domain.model;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class NeoSorter {
    public List<Neo> largest(List<Neo> neos, int count) {
        return find(neos, Comparator.comparingDouble(neo -> -neo.diameterKilometers()), count);
    }

    public List<Neo> closest(List<Neo> neos, int count) {
        return find(
                neos.stream().filter(neo -> !neo.approaches().isEmpty()).toList(),
                Comparator.comparingDouble(neo -> findClosestApproach(neo.approaches())),
                count
        );
    }

    private Double findClosestApproach(List<Approach> approaches) {
        return approaches.stream()
                .mapToDouble(Approach::missDistanceKilometers)
                .min()
                .getAsDouble();
    }

    private List<Neo> find(List<Neo> neos, Comparator<Neo> comparator, int count) {
        return neos.stream()
                .sorted(comparator)
                .limit(count)
                .toList();
    }
}
