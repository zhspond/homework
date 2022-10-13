package com.acme.asteroidhunt;

public class AsteroidhuntException extends RuntimeException {
    public AsteroidhuntException() {
    }

    public AsteroidhuntException(String message) {
        super(message);
    }

    public AsteroidhuntException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsteroidhuntException(Throwable cause) {
        super(cause);
    }
}
