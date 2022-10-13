package com.acme.asteroidhunt.infrastructure.nasa;

import com.acme.asteroidhunt.domain.model.Neo;
import com.acme.asteroidhunt.test.ClasspathReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NasaNeoParserTest {
    private final NasaNeoParser parser = new NasaNeoParser();

    @Test
    void parse_neos() {
        List<Neo> neo = parser.parseNeos(ClasspathReader.asString("nasaneosresponse.json"));
        assertThat(neo).hasSize(25);
        assertThat(neo.get(0).approaches()).hasSize(1);
        assertThat(neo.get(0).diameterKilometers()).isEqualTo(0.5035469604);
        // TODO more assertions on content, different samples
    }

    @Test
    void parse_characteristics() {
        var characteristics = parser.parseCharacteristics(ClasspathReader.asString("nasadetailsresponse.json"));
        assertThat(characteristics.id()).isEqualTo("3542519");
        // TODO more assertions to confirm correctness
    }
}
