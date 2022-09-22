package ru.practicum.exploreWithMe.controller;

import ru.practicum.exploreWithMe.auxiliaryObjects.client.EventClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exploreWithMe.dto.EventShortDtoOutput;
import ru.practicum.exploreWithMe.service.EventService;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventClient eventClient; // client which will be called for collect stat information
    private final EventService eventService;

    @Autowired
    public EventController(EventClient eventClient, EventService eventService) {
        this.eventClient = eventClient;
        this.eventService = eventService;
    }

    @GetMapping
    public EventShortDtoOutput getEvent() {
        // 1. before return request from main service (EWN), we claim to stat service
        eventClient.post("/hit", null); // here request will be redirected to stat service go through
        // controller, at that controller will be return value is void, in order to don't show stat information to user
        return eventService.getEvent();
    }
}
