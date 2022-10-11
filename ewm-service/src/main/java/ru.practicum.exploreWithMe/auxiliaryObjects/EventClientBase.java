package ru.practicum.exploreWithMe.auxiliaryObjects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class EventClientBase {
    protected final RestTemplate rest;

    public EventClientBase(RestTemplate rest) {
        this.rest = rest;
    }
    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, body);
    }
}
