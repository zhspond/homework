package com.acme.asteroidhunt.infrastructure.nasa;

import com.acme.asteroidhunt.AsteroidhuntException;
import com.acme.asteroidhunt.domain.model.Approach;
import com.acme.asteroidhunt.domain.model.Neo;
import com.acme.asteroidhunt.domain.model.NeoCharacteristics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// TODO check whether parsing can be done more elegantly
@Component
public class NasaNeoParser {
    private static final ObjectMapper om = new ObjectMapper();

    public List<Neo> parseNeos(String response) {
        var json = toJson(response);
        JsonNode neosPerDates = json.get("near_earth_objects");

        // done in two phases to quickly handle same asteroid appearing in the response on multiple dates
        ListMultimap<String, Approach> approaches = collectApproaches(stream(neosPerDates.elements()));
        Map<String, Neo> neosWithoutApproaches = parseNeos(stream(neosPerDates.elements()));

        return enrichNeosWithApproaches(neosWithoutApproaches, approaches);
    }

    private Map<String, Neo> parseNeos(Stream<JsonNode> neosPerDates) {
        Map<String, Neo> neos = new LinkedHashMap<>();
        neosPerDates.forEach(
                neosPerDate -> stream(neosPerDate.elements()).forEach(
                        neoNode -> neos.put(
                                neoNode.get("id").asText(),
                                new Neo(
                                        neoNode.get("id").asText(),
                                        neoNode.get("name").asText(),
                                        neoNode.get("estimated_diameter").get("kilometers").get("estimated_diameter_max").asDouble(),
                                        List.of()
                                )
                        )
                )
        );
        return Collections.unmodifiableMap(neos);
    }

    public NeoCharacteristics parseCharacteristics(String response) {
        var json = toJson(response);
        return new NeoCharacteristics(
                json.get("id").asText(),
                json.get("name").asText(),
                json.get("absolute_magnitude_h").asDouble(),
                json.get("designation").asText(),
                json.get("estimated_diameter").get("kilometers").get("estimated_diameter_min").asDouble(),
                json.get("estimated_diameter").get("kilometers").get("estimated_diameter_max").asDouble()
        );
    }

    private ListMultimap<String, Approach> collectApproaches(Stream<JsonNode> neosPerDates) {
        ListMultimap<String, Approach> approaches = MultimapBuilder.hashKeys().arrayListValues().build();
        neosPerDates.forEach(
                neosPerDate -> stream(neosPerDate.elements()).forEach(neoNode ->
                        stream(neoNode.get("close_approach_data").elements()).forEach(
                                approachNode -> {
                                    var approach = new Approach(approachNode.get("miss_distance").get("kilometers").asDouble());
                                    approaches.put(neoNode.get("id").asText(), approach);
                                }
                        )));
        return approaches;
    }

    private List<Neo> enrichNeosWithApproaches(Map<String, Neo> neosWithoutApproaches, ListMultimap<String, Approach> approaches) {
        return neosWithoutApproaches.values().stream().map(
                // i wish this was kotlin :)
                neo -> new Neo(neo.id(), neo.name(), neo.diameterKilometers(), approaches.get(neo.id()))
        ).toList();
    }

    private JsonNode toJson(String response) {
        try {
            return om.readTree(response);
        } catch (JsonProcessingException e) {
            throw new AsteroidhuntException("Failed to parse response", e);
        }
    }

    private Stream<JsonNode> stream(Iterator<JsonNode> nodes) {
        Iterable<JsonNode> iterable = () -> nodes;
        return StreamSupport.stream(iterable.spliterator(), false);
    }


}