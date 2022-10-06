package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.auxiliaryObjects.StatusOfParticipationRequest;
import ru.practicum.exploreWithMe.dto.ParticipationRequestDtoOutput;
import ru.practicum.exploreWithMe.mapper.ParticipationRequestMapper;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.ParticipationRequest;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.ParticipationRequestRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Component("ParticipationRequestServiceInDB")
@RequiredArgsConstructor
public class ParticipationRequestServiceInDB implements ParticipationRequestServices {
    @Autowired
    private final ParticipationRequestRepository participationRequestRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    @Qualifier("ParticipationRequestMapperImplForBD")
    final ParticipationRequestMapper participationRequestMapper;

    @Override
    public List<ParticipationRequestDtoOutput> getRequestPrivate(Long userId) {
        log.debug("Return all participationRequest for Requester by path : '/users/{userId}/requests'");
        return participationRequestRepository.getAllByRequestorId(userId).stream()
                .map(participationRequestMapper::RequestDtoOutputFromParticipationRequest)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDtoOutput createRequestPrivate(Long userId, Long eventId) {
        log.debug("Create participationRequest for Requester by path : '/users/{userId}/requests'");
        // 1. Получаем событие
        Event event = eventRepository.getReferenceById(eventId);
        if (event.getRequestModeration().equals(true) || event.getParticipantLimit() > 0) {
            ParticipationRequest participationRequest = participationRequestRepository.save(
                    new ParticipationRequest(
                            0L,
                            eventRepository.getReferenceById(eventId),
                            LocalDateTime.now(),
                            userRepository.getReferenceById(userId),
                            StatusOfParticipationRequest.PENDING
                    )
            );
            return participationRequestMapper.RequestDtoOutputFromParticipationRequest(participationRequest);
        }
        return null;
    }

    @Override
    public ParticipationRequestDtoOutput cancelOwnRequestPrivate(Long userId, Long requestId) {
        log.debug("Requester refuse from participationRequest by path : '/users/{userId}/requests/{requestId}/cancel'");
        participationRequestRepository.cancelOwnRequest(
                userId, requestId, StatusOfParticipationRequest.REJECTED
        );
        return participationRequestMapper.RequestDtoOutputFromParticipationRequest(
                participationRequestRepository.getByIdAndRequestorId(requestId, userId)
        );
    }
}
