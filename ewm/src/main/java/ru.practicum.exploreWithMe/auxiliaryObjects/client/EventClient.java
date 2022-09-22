package ru.practicum.exploreWithMe.auxiliaryObjects.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.exploreWithMe.dto.EventShortDtoOutput;

@Service
public class EventClient {
    // This client sent request
    private static final String API_PREFIX = "/hit";
    final RestTemplate rest;

    @Autowired
    public EventClient(@Value("${exploreWithMeStatService.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> post(String path, EventShortDtoOutput body) {
        return post(path, 1, null);
    }

    public ResponseEntity<Object> post(String path, long userId, EventShortDtoOutput body) {
        return post("", userId, body);
    }

}
