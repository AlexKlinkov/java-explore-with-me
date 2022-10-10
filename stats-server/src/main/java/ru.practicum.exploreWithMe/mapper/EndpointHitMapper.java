package ru.practicum.exploreWithMe.mapper;

import org.mapstruct.Mapper;
import ru.practicum.exploreWithMe.dto.EndpointHitDTOInput;
import ru.practicum.exploreWithMe.dto.EndpointHitDtoOutput;
import ru.practicum.exploreWithMe.model.EndpointHit;

@Mapper
public interface EndpointHitMapper {
    EndpointHitDtoOutput endPointHitDtoOutputFromEndpointHit(EndpointHit endpointHit);
}
