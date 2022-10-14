package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.explore_with_me.auxiliary_objects.StatInfoInput;
import ru.practicum.explore_with_me.dto.EventFullDtoOutput;
import ru.practicum.explore_with_me.dto.EventShortDtoOutput;
import ru.practicum.explore_with_me.dto.NewEventDTOInput;
import ru.practicum.explore_with_me.dto.ParticipationRequestDtoOutput;
import ru.practicum.explore_with_me.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {
    @Autowired
    @Qualifier("EventServiceInBD")
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventShortDtoOutput> getEventPublic(@RequestParam(value = "text", required = false) String text,
                                                    @RequestParam(value = "categories", required = false,
                                                      defaultValue = "false") List<Long> categories,
                                                    @RequestParam(value = "paid", required = false, defaultValue = "false")
                                                        Boolean paid,
                                                    @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                                    @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                                    @RequestParam(value = "onlyAvailable", required = false,
                                                      defaultValue = "false") Boolean onlyAvailable,
                                                    @RequestParam(value = "sort", required = false,
                                                      defaultValue = "EVENT_DATE") String sortEventDateOrViews,
                                                    @RequestParam(value = "from", required = false, defaultValue = "0")
                                                  Long from,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10")
                                                  Long size, HttpServletRequest request) {
        List<EventShortDtoOutput> eventsForReturn = eventService.getEventsPublic(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sortEventDateOrViews, from, size);
        // 1. before return request from main service (EWN), we invoke to stat service
        // here request will be redirected to stat service
        // go through controller, at that controller will be return value is void, in order to
        // don't show stat information to user
        WebClient.create()
                .post()
                .uri("http://localhost:9090" + "/hit")
                .body(Mono.just(new StatInfoInput("ewn-service", request.getRequestURI(),
                        request.getRemoteAddr(), LocalDateTime.now().toString())), StatInfoInput.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StatInfoInput.class);
        return eventsForReturn;
    }

    @GetMapping("/events/{id}")
    public EventFullDtoOutput getEventByIdPublic(@PathVariable Long id) {
        EventFullDtoOutput eventsForReturn = eventService.getEventByIdPublic(id);
        return eventsForReturn;
    }

    @GetMapping("/users/{userId}/events")
    public List<EventFullDtoOutput> getEventsPrivate(@PathVariable Long userId,
                                                    @RequestParam(value = "from", required = false, defaultValue = "0")
                                                    Long from,
                                                     @RequestParam(value = "size", required = false, defaultValue = "10")
                                                     Long size) {
        return eventService.getEventsPrivate(userId, from, size);
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDtoOutput updateEventPrivate(@PathVariable Long userId,
                                                 @RequestBody NewEventDTOInput newEventDTOInput) {
        return eventService.updateEventPrivate(userId, newEventDTOInput);
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDtoOutput createEventPrivate(@PathVariable Long userId,
                                                 @RequestBody NewEventDTOInput newEventDTOInput) {
        return eventService.createEventPrivate(userId, newEventDTOInput);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDtoOutput getFullInfoAboutEventByUserWhoCreatedThisEventPrivate(@PathVariable Long userId,
                                                                 @PathVariable Long eventId) {
        return eventService.getFullInfoAboutEventByUserWhoCreatedThisEventPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDtoOutput cancelEventPrivate(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.cancelEventPrivate(userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDtoOutput> getParticipationInformationAboutUserPrivate(@PathVariable Long userId,
                                                                                           @PathVariable Long eventId) {
        return eventService.getParticipationInformationAboutUserPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDtoOutput approveParticipationRequestPrivate(@PathVariable Long userId,
                                                                            @PathVariable Long eventId,
                                                                            @PathVariable Long reqId) {
        return eventService.approveParticipationRequestPrivate(userId, eventId, reqId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDtoOutput refuseParticipationRequestPrivate(@PathVariable Long userId,
                                                                            @PathVariable Long eventId,
                                                                            @PathVariable Long reqId) {
        return eventService.refuseParticipationRequestPrivate(userId, eventId, reqId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDtoOutput> getEventsAdmin (@RequestParam(name = "users", required = false) List<Long> users,
                                                    @RequestParam(name = "states", required = false)
                                                    List<String> states,
                                                    @RequestParam(name = "categories", required = false)
                                                        List<Long> categories,
                                                    @RequestParam(name = "rangeStart", required = false)
                                                    String rangeStart,
                                                    @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                    @RequestParam(name = "from", required = false,
                                                            defaultValue = "0") Long from,
                                                    @RequestParam(name = "size", required = false,
                                                            defaultValue = "10") Long size) {

        return eventService.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDtoOutput editEventByAdmin (@PathVariable Long eventId,
                                                @RequestBody NewEventDTOInput newEventDTOInput) {
        return eventService.editEventByAdmin(eventId, newEventDTOInput);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    EventFullDtoOutput publishEventByAdmin (@PathVariable Long eventId) {
        return eventService.publishEventByAdmin(eventId);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    EventFullDtoOutput rejectPublishEventByAdmin (@PathVariable Long eventId) {
        return eventService.rejectPublishEventByAdmin(eventId);
    }
}
