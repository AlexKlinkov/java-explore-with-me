package ru.practicum.explore_with_me.auxiliary_objects;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class StatsClient {
    public void StatsClient(String uri, String address) {
        WebClient.create()
                .post()
                .uri("http://localhost:9090" + "/hit")
                .body(Mono.just(new StatInfoInput("ewn-service", uri,
                        address, LocalDateTime.now().toString())), StatInfoInput.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StatInfoInput.class);
    }
}
