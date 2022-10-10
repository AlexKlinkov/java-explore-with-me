package ru.practicum.exploreWithMe.mapper;

import ru.practicum.exploreWithMe.dto.EndpointHitDtoOutput;
import ru.practicum.exploreWithMe.model.EndpointHit;

public class EndpointHitMapperForBD implements EndpointHitMapper{

    @Override
    public EndpointHitDtoOutput endPointHitDtoOutputFromEndpointHit(EndpointHit endpointHit) {
        if (endpointHit == null) {
            return null;
        }
        EndpointHitDtoOutput endpointHitDtoOutput = new EndpointHitDtoOutput();
        endpointHitDtoOutput.setId(endpointHit.getId());
        endpointHitDtoOutput.setApp(endpointHit.getApp());
        endpointHitDtoOutput.setIp(endpointHit.getAttributes().getIp());
        endpointHitDtoOutput.setUri(endpointHit.getAttributes().getUri());
        endpointHitDtoOutput.setTimestamp(endpointHit.getTimestamp());
        return endpointHitDtoOutput;
    }
}
