package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.EventFullDtoOutput;
import ru.practicum.explore_with_me.dto.EventShortDtoOutput;
import ru.practicum.explore_with_me.dto.NewEventDTOInput;
import ru.practicum.explore_with_me.model.Event;

@Mapper
public interface EventMapper {
    Event eventFromNewEventDTOInput(NewEventDTOInput newEventDTOInput);
    EventShortDtoOutput eventShortDtoOutputFromEvent(Event event);
    EventFullDtoOutput eventFullDtoOutputFromEvent(Event event);
}
