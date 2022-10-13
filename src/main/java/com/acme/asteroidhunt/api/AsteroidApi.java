package com.acme.asteroidhunt.api;

import com.acme.asteroidhunt.domain.model.DateRange;
import com.acme.asteroidhunt.domain.model.Neo;
import com.acme.asteroidhunt.domain.model.NeoCharacteristics;
import com.acme.asteroidhunt.domain.service.NeoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/*
       TODO Domain object used as API payload directly, usually a bad idea.
       Leaving it as is due to time running out, a layer of DTOs would probably be a good idea
*/
@RestController
public class AsteroidApi {
    private final NeoService neoService;

    public AsteroidApi(NeoService neoService) {
        this.neoService = neoService;
    }


    @GetMapping("/asteroid/closest")
    public List<Neo> closestAsteroids(@RequestParam("start") String start, @RequestParam("end") String end) {
        return neoService.findClosestNeos(verifiedRange(start, end));
    }

    @GetMapping("/asteroid/largest")
    public Optional<NeoCharacteristics> largestAsteroid(@RequestParam("start") String start, @RequestParam("end") String end) {
        return neoService.findCharacteristicsOfLargestNeo(verifiedRange(start, end));
    }

    private DateRange verifiedRange(String startString, String endString) {
        final LocalDate start;
        final LocalDate end;
        try {
            start = LocalDate.parse(startString);
            end = LocalDate.parse(endString);
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid format for 'start' or 'end'");
        }
        if(end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'end' cannot be before 'start'");
        }
        // TODO alliviate this constraint with multiple queries against nasa?
        if(end.minusDays(7).isAfter(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Max duration of time period is 7 days");
        }
        return new DateRange(start, end);
    }
}
