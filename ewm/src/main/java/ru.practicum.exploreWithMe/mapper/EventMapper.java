package ru.practicum.exploreWithMe.mapper;

import org.mapstruct.Mapper;
import ru.practicum.exploreWithMe.dto.EventFullDtoOutput;
import ru.practicum.exploreWithMe.dto.EventShortDtoOutput;
import ru.practicum.exploreWithMe.dto.NewEventDTOInput;
import ru.practicum.exploreWithMe.model.Event;

@Mapper
public interface EventMapper {
    Event eventFromNewEventDTOInput(NewEventDTOInput newEventDTOInput);
    EventShortDtoOutput eventShortDtoOutputFromEvent(Event event);
    EventFullDtoOutput eventFullDtoOutputFromEvent(Event event);
}
