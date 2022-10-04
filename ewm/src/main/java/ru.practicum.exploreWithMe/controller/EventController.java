package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.auxiliaryObjects.client.EventClient;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.exploreWithMe.dto.EventFullDtoOutput;
import ru.practicum.exploreWithMe.dto.EventShortDtoOutput;
import ru.practicum.exploreWithMe.dto.NewEventDTOInput;
import ru.practicum.exploreWithMe.service.EventService;
import ru.practicum.exploreWithMe.service.EventServiceInBD;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {
    @Autowired
    private final EventClient eventClient; // client which will be called for collect stat information
    @Autowired
    @Qualifier("EventServiceInBD")
    private final EventService eventService;
    @GetMapping("/events")
    public List<EventShortDtoOutput> getEventPublic(@RequestParam(value = "text", required = false) String text,
                                              @RequestParam(value = "categories", required = false,
                                                      defaultValue = "false") List<Long> categories,
                                              @RequestParam(value = "paid", required = false) Boolean paid,
                                              @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                              @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                              @RequestParam(value = "onlyAvailable", required = false,
                                                      defaultValue = "false") Boolean onlyAvailable,
                                              @RequestParam(value = "sort", required = false,
                                                      defaultValue = "EVENT_DATE") String sortEventDateOrViews,
                                              @RequestParam(value = "from", required = false, defaultValue = "0")
                                                  Long from,
                                              @RequestParam(value = "size", required = false, defaultValue = "10")
                                                  Long size) {
        List<EventShortDtoOutput> eventsForReturn = eventService.getEventsPublic(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sortEventDateOrViews, from, size);
        // 1. before return request from main service (EWN), we claim to stat service
        //eventClient.post("/hit", eventsForReturn); // here request will be redirected to stat service go through
        // controller, at that controller will be return value is void, in order to don't show stat information to user
        return eventsForReturn;
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDtoOutput createEventPrivate(@PathVariable Long userId, @Valid NewEventDTOInput newEventDTOInput) {
        System.out.println("Here controller");
        return eventService.createEventPrivate(userId, newEventDTOInput);
    }
}
