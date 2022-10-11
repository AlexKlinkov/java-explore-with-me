package ru.practicum.exploreWithMe.auxiliaryObjects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;


@Service
public class EventClient extends EventClientBase {
    // This client sent request
    private final static String API_PREFIX = "/hit";

    @Autowired
    public EventClient(@Value("${exploreWithMeStatService.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }
    public ResponseEntity<Object> create(StatInfoInput body) {
        return post("", body);
    }
}
