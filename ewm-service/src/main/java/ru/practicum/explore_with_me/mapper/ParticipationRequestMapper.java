package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.ParticipationRequestDtoOutput;
import ru.practicum.explore_with_me.model.ParticipationRequest;

@Mapper
public interface ParticipationRequestMapper {
    ParticipationRequestDtoOutput RequestDtoOutputFromParticipationRequest(ParticipationRequest participationRequest);
}
