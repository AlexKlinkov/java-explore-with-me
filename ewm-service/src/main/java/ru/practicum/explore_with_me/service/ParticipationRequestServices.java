package ru.practicum.explore_with_me.service;


import ru.practicum.explore_with_me.dto.ParticipationRequestDtoOutput;

import java.util.List;

public interface ParticipationRequestServices {
    List<ParticipationRequestDtoOutput> getRequestPrivate(Long userId); // GET /users/{userId}/requests
    // request has to be unique
    // initiator of this event cannot apply on taking part in own event
    // no one takes a part in unpublished event
    // if limit of participation was achieved should be returned mistake
    // if event doesn't have pre moderation rules this request has to be taken automatically
    ParticipationRequestDtoOutput createRequestPrivate(Long userId, Long eventId); // POST /users/{userId}/requests
    // PATCH /users/{userId}/requests/{requestId}/cancel
    ParticipationRequestDtoOutput cancelOwnRequestPrivate(Long userId, Long requestId);
}
