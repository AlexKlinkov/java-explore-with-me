package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.auxiliaryObjects.StatusOfEvent;
import ru.practicum.exploreWithMe.dto.EventFullDtoOutput;
import ru.practicum.exploreWithMe.dto.EventShortDtoOutput;
import ru.practicum.exploreWithMe.dto.NewEventDTOInput;
import ru.practicum.exploreWithMe.dto.ParticipationRequestDtoOutput;
import ru.practicum.exploreWithMe.mapper.EventMapper;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Component("EventServiceInBD")
@RequiredArgsConstructor
public class EventServiceInBD implements EventService{
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    @Qualifier("EventMapperImplForBD")
    final EventMapper eventMapper;

    @Override
    public List<EventShortDtoOutput> getEventsPublic(String text, List<Long> categoryIds, Boolean paid,
                                                     String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                     String sortEventDateOrViews, Long from, Long size) {
        return null;
    }

    @Override
    public EventFullDtoOutput getEventByIdPublic(Long eventId) {
        return null;
    }

    @Override
    public List<EventShortDtoOutput> getEventsPrivate(Long userId, Long from, Long size) {
        return null;
    }

    @Override
    public EventFullDtoOutput updateEventPrivate(Long userId, NewEventDTOInput newEventDTOInput) {
        return null;
    }

    @Override
    public EventFullDtoOutput createEventPrivate(Long userId, NewEventDTOInput newEventDTOInput) {
        log.debug("Create event by path : '/users/{userId}/events'");
        System.out.println("0");
        Event event = eventMapper.eventFromNewEventDTOInput(newEventDTOInput);
        System.out.println("1");
        event.setCreatedOn(LocalDateTime.now());
        System.out.println("2");
        event.setInitiator(userRepository.getReferenceById(userId));
        System.out.println("3");
        event.setState(StatusOfEvent.PENDING);
        System.out.println("4");
        event.setViews(0L);
        System.out.println("5");
        Event almostFullEvent = eventRepository.save(event);
        System.out.println("6");
        return eventMapper.eventFullDtoOutputFromEvent(almostFullEvent);
    }

    @Override
    public EventFullDtoOutput getFullInfoAboutEventByUserWhoCreatedThisEventPrivate(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDtoOutput cancelEventPrivate(Long userId, NewEventDTOInput newEventDTOInput) {
        return null;
    }

    @Override
    public ParticipationRequestDtoOutput getParticipationInformationAboutUserPrivate(Long userId, Long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDtoOutput ApproveParticipationRequestPrivate(Long userId, Long eventId, Long reqId) {
        return null;
    }

    @Override
    public ParticipationRequestDtoOutput refuseParticipationRequestPrivate(Long userId, Long eventId, Long reqId) {
        return null;
    }

    @Override
    public List<EventFullDtoOutput> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                   String rangeStart, String rangeEnd, Long from, Long size) {
        return null;
    }

    @Override
    public EventShortDtoOutput editEventByAdmin(Long eventId) {
        return null;
    }

    @Override
    public EventFullDtoOutput publishEventByAdmin(Long eventId) {
        return null;
    }

    @Override
    public EventFullDtoOutput rejectPublishEventByAdmin(Long eventId) {
        return null;
    }
}
