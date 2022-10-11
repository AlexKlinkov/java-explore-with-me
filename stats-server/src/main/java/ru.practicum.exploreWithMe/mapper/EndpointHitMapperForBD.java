package ru.practicum.exploreWithMe.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.EndpointHitDtoOutput;
import ru.practicum.exploreWithMe.model.EndpointHit;

@Data
@Slf4j
@Component("EndpointHitMapperForBD")
@RequiredArgsConstructor
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
