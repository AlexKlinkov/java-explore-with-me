package ru.practicum.exploreWithMe.mapper;

import org.mapstruct.Mapper;
import ru.practicum.exploreWithMe.dto.ParticipationRequestDtoOutput;
import ru.practicum.exploreWithMe.model.ParticipationRequest;

@Mapper
public interface ParticipationRequestMapper {
    ParticipationRequestDtoOutput RequestDtoOutputFromParticipationRequest(ParticipationRequest participationRequest);
}
