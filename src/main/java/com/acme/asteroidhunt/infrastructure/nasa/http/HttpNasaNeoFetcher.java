package com.acme.asteroidhunt.infrastructure.nasa.http;

import com.acme.asteroidhunt.AsteroidhuntException;
import com.acme.asteroidhunt.domain.model.DateRange;
import com.acme.asteroidhunt.infrastructure.nasa.NasaNeoFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

@Service
public class HttpNasaNeoFetcher implements NasaNeoFetcher {
    private final String baseUrl;
    private final String apiKey;
    private final HttpClient client;

    public HttpNasaNeoFetcher(
            @Value("${nasa.base.url}") String baseUrl,
            @Value("${nasa.api.key}") String apiKey
    ) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    @Override
    public String fetchNeos(DateRange range) {
        String path = "/feed?start_date=%s&end_date=%s&api_key=%s".formatted(range.start(), range.end(), apiKey);
        var response = getResponse(path);
        failOnNon200(response);
        return response.body();
    }

    @Override
    public Optional<String> fetchDetailsOfNeo(String id) {
        String path = "/neo/%s?api_key=%s".formatted(id, apiKey);
        var response = getResponse(path);
        if(response.statusCode() == 404) {
            return Optional.empty();
        }
        failOnNon200(response);
        return Optional.of(response.body());
    }

    private void failOnNon200(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            throw new AsteroidhuntException("Unexpected response code: %s, body: %s".formatted(response.statusCode(), response.body()));
        }
    }

    private HttpResponse<String> getResponse(String path) {
        HttpResponse<String> response;
        try {
            response = client.send(HttpRequest.newBuilder().uri(URI.create(baseUrl + path)).GET().build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new AsteroidhuntException(e);
        }
        return response;

    }
}