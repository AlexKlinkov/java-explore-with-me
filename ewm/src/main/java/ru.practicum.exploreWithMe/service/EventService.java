package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.EventFullDtoOutput;
import ru.practicum.exploreWithMe.dto.EventShortDtoOutput;
import ru.practicum.exploreWithMe.dto.NewEventDTOInput;
import ru.practicum.exploreWithMe.dto.ParticipationRequestDtoOutput;

import java.util.List;

public interface EventService {
    // This method return only PUBLISHED events according chosen params:
    // param 'text' can help find necessary event by search annotation or description not take account register of words
    // param 'categories' ids belong to entity 'category' indicates which events have to be chosen by categories
    // param 'paid' indicates event will be free or for the money
    // params 'rangeStart' and 'rangeEnd' period of events which will be showed
    // param 'onlyAvailable' indicates there are a free place to visit this event or not
    // param 'sortEventDateOrViews' are assumed sorting by date or amount of views this event
    // param 'from' indicates quantity of element which have to be skipped
    // param 'size' indicates quantity of element in set
    // information about this method was invoked have to be saved in stat service
    List<EventShortDtoOutput> getEventsPublic(String text, List<Long> categoryIds, Boolean paid, String rangeStart,
                                        String rangeEnd, Boolean onlyAvailable, String sortEventDateOrViews,
                                        Long from, Long size); // GET /events (PUBLIC)

    // This method return only PUBLISHED event by ID
    // information about this method was invoked have to be saved in stat service
    EventFullDtoOutput getEventByIdPublic(Long eventId); // GET /events/{eventId} (PUBLIC)
    // GET /users/{userId}/events (PRIVATE)
    List<EventShortDtoOutput> getEventsPrivate (Long userId, Long from, Long size);
    // only refused events or waiting moderation by admin can be changed
    // PATCH /users/{userId}/events (PRIVATE)
    EventFullDtoOutput updateEventPrivate(Long userId, NewEventDTOInput newEventDTOInput);
    // date (start) of this event cannot be less than in 2 hours after created this event
    // POST /users/{userId}/events (PRIVATE)
    EventFullDtoOutput createEventPrivate(Long userId, NewEventDTOInput newEventDTOInput);
    // GET /users/{userId}/events/{eventId} (PRIVATE)
    EventFullDtoOutput getFullInfoAboutEventByUserWhoCreatedThisEventPrivate(Long userId, Long eventId);
    // only events is waited moderation by admin can be canceled
    // PATCH /users/{userId}/events/{eventId} (PRIVATE)
    EventFullDtoOutput cancelEventPrivate(Long userId, NewEventDTOInput newEventDTOInput);
    // GET /users/{userId}/events/{eventId}/requests (PRIVATE)
    ParticipationRequestDtoOutput getParticipationInformationAboutUserPrivate(Long userId, Long eventId);
    // if ParticipationLimit is 0 or pre moderation authorization is turn off,
    // that means approving of request is not necessary.
    // if ParticipationLimit was achieved, request cannot be approved and leftover of requests should be canceled
    // PATCH /users/{userId}/events/{eventId}/requests/{reqId}/confirm (PRIVATE)
    ParticipationRequestDtoOutput ApproveParticipationRequestPrivate(Long userId, Long eventId, Long reqId);
    // PATCH /users/{userId}/events/{eventId}/requests/{reqId}/reject (PRIVATE)
    ParticipationRequestDtoOutput refuseParticipationRequestPrivate(Long userId, Long eventId, Long reqId);
    // param 'users' list of usersID using which need to find events
    // param 'states' list of StatusOfEvent using which need to find events
    // param 'categories' list of categoryID using which need to find events
    // param 'rangeStart' date indicates date when this event has to happen (after this date or in that)
    // param 'rangeEnd' date indicates date till which moment this event has to finished (before this date or in that)
    // param 'from' indicates quantity of element which have to be skipped
    // param 'size' indicates quantity of element in set
    // GET /admin/events
    List<EventFullDtoOutput> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                            String rangeStart, String rangeEnd, Long from, Long size);
    // PUT admin/events/{eventId}
    EventFullDtoOutput editEventByAdmin(Long eventId, NewEventDTOInput newEventDTOInput);
    // date of start event has to be minimum for 1 hours till published date
    // status of event has to be PENDING
    // PATCH admin/events/{eventId}/publish
    EventFullDtoOutput publishEventByAdmin(Long eventId);
    // event doesn't have status PUBLISHED
    // PATCH admin/events/{eventId}/publish
    EventFullDtoOutput rejectPublishEventByAdmin(Long eventId);
}
