package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.auxiliary_objects.CompilationCheckValidationMethods;
import ru.practicum.explore_with_me.auxiliary_objects.StatusOfParticipationRequest;
import ru.practicum.explore_with_me.dto.ParticipationRequestDtoOutput;
import ru.practicum.explore_with_me.exeption.ConflictException;
import ru.practicum.explore_with_me.exeption.NotFoundException;
import ru.practicum.explore_with_me.exeption.ServerError;
import ru.practicum.explore_with_me.exeption.ValidationException;
import ru.practicum.explore_with_me.mapper.ParticipationRequestMapper;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.ParticipationRequest;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.ParticipationRequestRepository;
import ru.practicum.explore_with_me.repository.UserRepository;

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
        if (CompilationCheckValidationMethods.checkParamOfId(userId)) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        try {
            return participationRequestRepository.getAllByRequestorId(userId).stream()
                    .map(participationRequestMapper::RequestDtoOutputFromParticipationRequest)
                    .collect(Collectors.toList());
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public ParticipationRequestDtoOutput createRequestPrivate(Long userId, Long eventId) {
        log.debug("Create participationRequest for Requester by path : '/users/{userId}/requests'");
        if (CompilationCheckValidationMethods.checkParamOfId(userId)) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        if (CompilationCheckValidationMethods.checkParamOfId(eventId)) {
            throw new ValidationException("Id of event cannot be less than 0");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        if (participationRequestRepository.findByRequestorIdAndEventId(userId, eventId) != null) {
            throw new ConflictException("Request from user with id=" + userId + " on visit event with id=" + eventId +
                    " was already created");
        }
        try {
            LocalDateTime now = LocalDateTime.now();
            // 1. Получаем событие
            Event event = eventRepository.getReferenceById(eventId);
            if (event.getRequestModeration().equals(true) || event.getParticipantLimit() > 0) {
                ParticipationRequest participationRequest = new ParticipationRequest();
                participationRequest.setEvent(eventRepository.getReferenceById(eventId));
                participationRequest.setRequestor(userRepository.getReferenceById(userId));
                participationRequest.setStatus(StatusOfParticipationRequest.PENDING);
                participationRequest.setCreated(now);
                return participationRequestMapper.RequestDtoOutputFromParticipationRequest(
                        participationRequestRepository.save(participationRequest));
            }
            return null;
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public ParticipationRequestDtoOutput cancelOwnRequestPrivate(Long userId, Long requestId) {
        log.debug("Requester refuse from participationRequest by path : '/users/{userId}/requests/{requestId}/cancel'");
        if (CompilationCheckValidationMethods.checkParamOfId(userId)) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        if (CompilationCheckValidationMethods.checkParamOfId(requestId)) {
            throw new ValidationException("Id of request cannot be less than 0");
        }
        if (!participationRequestRepository.existsById(requestId)) {
            throw new NotFoundException("Request with id=" + requestId + " was not found.");
        }
        if (!participationRequestRepository.getReferenceById(requestId).getRequestor().getId().equals(userId)) {
            throw new ConflictException("User with id=" + userId + " cannot refuse request with id=" + requestId +
                    " so this request is not belonged to him");
        } else {
            try {
                ParticipationRequest participationRequest = participationRequestRepository.getReferenceById(requestId);
                participationRequest.setStatus(StatusOfParticipationRequest.CANCELED);
                return participationRequestMapper.RequestDtoOutputFromParticipationRequest(
                        participationRequestRepository.save(participationRequest));
            } catch (ServerError exception) {
                throw new ServerError("Error occurred");
            }
        }
    }
}
