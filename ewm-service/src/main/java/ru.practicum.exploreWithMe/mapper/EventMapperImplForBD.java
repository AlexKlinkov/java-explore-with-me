package ru.practicum.exploreWithMe.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.auxiliaryObjects.Location;
import ru.practicum.exploreWithMe.dto.EventFullDtoOutput;
import ru.practicum.exploreWithMe.dto.EventShortDtoOutput;
import ru.practicum.exploreWithMe.dto.NewEventDTOInput;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.repository.CategoryRepository;

@Data
@Slf4j
@Component("EventMapperImplForBD")
@RequiredArgsConstructor
public class EventMapperImplForBD implements EventMapper{
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    @Qualifier("UserMapperImplForBD")
    final UserMapper userMapper;
    @Autowired
    @Qualifier("CategoryMapperImplForBD")
    final CategoryMapper categoryMapper;

    @Override
    public Event eventFromNewEventDTOInput(NewEventDTOInput newEventDTOInput) {
        if (newEventDTOInput == null) {
            return null;
        }
        Event event = new Event();
        event.setId(null);
        event.setEventDate(newEventDTOInput.getEventDate());
        event.setAnnotation(newEventDTOInput.getAnnotation());
        event.setDescription(newEventDTOInput.getDescription());
        event.setTitle(newEventDTOInput.getTitle());
        event.setPaid(newEventDTOInput.getPaid());
        event.setParticipantLimit(newEventDTOInput.getParticipantLimit());
        event.setLat(newEventDTOInput.getLocation().getLat());
        event.setLon(newEventDTOInput.getLocation().getLon());
        event.setRequestModeration(newEventDTOInput.getRequestModeration());
        if (newEventDTOInput.getCategoryId() != null) {
            event.setCategory(categoryRepository.getReferenceById(newEventDTOInput.getCategoryId()));
        } else {
            event.setCategory(null);
        }
        event.setConfirmedRequests(null);
        event.setCreatedOn(null);
        event.setInitiator(null);
        event.setPublishedOn(null);
        event.setState(null);
        event.setViews(null);
        return event;
    }

    @Override
    public EventShortDtoOutput eventShortDtoOutputFromEvent(Event event) {
        if (event == null) {
            return null;
        }

        EventShortDtoOutput eventShortDtoOutput = new EventShortDtoOutput();
        eventShortDtoOutput.setEventDate(event.getEventDate());
        eventShortDtoOutput.setInitiator(userMapper.userShortDtoOutputFromUser(event.getInitiator()));
        eventShortDtoOutput.setViews(event.getViews());
        eventShortDtoOutput.setAnnotation(event.getAnnotation());
        eventShortDtoOutput.setId(event.getId());
        eventShortDtoOutput.setCategory(categoryMapper.categoryDtoOutputFromCategory(event.getCategory()));
        eventShortDtoOutput.setPaid(event.getPaid());
        eventShortDtoOutput.setTitle(event.getTitle());
        event.setConfirmedRequests(event.getConfirmedRequests());
        return eventShortDtoOutput;
    }

    @Override
    public EventFullDtoOutput eventFullDtoOutputFromEvent(Event event) {
        if (event == null) {
            return null;
        }

        EventFullDtoOutput eventFullDtoOutput = new EventFullDtoOutput();
        eventFullDtoOutput.setId(event.getId());
        eventFullDtoOutput.setAnnotation(event.getAnnotation());
        eventFullDtoOutput.setEventDate(event.getEventDate());
        eventFullDtoOutput.setTitle(event.getTitle());
        eventFullDtoOutput.setDescription(event.getDescription());
        eventFullDtoOutput.setPaid(event.getPaid());
        eventFullDtoOutput.setParticipantLimit(event.getParticipantLimit());
        eventFullDtoOutput.setLocation(new Location(event.getLat(), event.getLon()));
        eventFullDtoOutput.setRequestModeration(event.getRequestModeration());
        eventFullDtoOutput.setCategory(categoryMapper.categoryDtoOutputFromCategory(event.getCategory()));
        eventFullDtoOutput.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDtoOutput.setCreatedOn(event.getCreatedOn());
        eventFullDtoOutput.setInitiator(userMapper.userShortDtoOutputFromUser(event.getInitiator()));
        eventFullDtoOutput.setPublishedOn(event.getPublishedOn());
        eventFullDtoOutput.setState(event.getState());
        eventFullDtoOutput.setViews(event.getViews());
        return eventFullDtoOutput;
    }
}
