package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.ParticipationRequestDtoOutput;
import ru.practicum.explore_with_me.service.ParticipationRequestServices;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ParticipationRequestController {
    @Autowired
    @Qualifier("ParticipationRequestServiceInDB")
    private final ParticipationRequestServices participationRequestServices;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDtoOutput> getRequestsByUserId (@PathVariable Long userId) {
        return participationRequestServices.getRequestPrivate(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDtoOutput createRequestByUserIdAndEventId (@PathVariable Long userId,
                                                                          @RequestParam(value = "eventId") Long eventId) {
        return participationRequestServices.createRequestPrivate(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDtoOutput cancelRequestOnParticipationByRequestor(@PathVariable Long userId,
                                                                                 @PathVariable Long requestId) {
        return participationRequestServices.cancelOwnRequestPrivate(userId, requestId);
    }
}
