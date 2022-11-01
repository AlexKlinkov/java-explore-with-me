package explore_with_me.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import explore_with_me.dto.EndpointHitDtoOutput;
import explore_with_me.model.EndpointHit;

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
