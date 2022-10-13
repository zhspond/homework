package com.acme.asteroidhunt.test;

import com.acme.asteroidhunt.AsteroidhuntException;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ClasspathReader {
    public static String asString(String path) {
        try {
            URL url = Resources.getResource(path);
            return Resources.toString(url, StandardCharsets.UTF_8);
        } catch(IOException e) {
            throw new AsteroidhuntException(e);
        }
    }
}
