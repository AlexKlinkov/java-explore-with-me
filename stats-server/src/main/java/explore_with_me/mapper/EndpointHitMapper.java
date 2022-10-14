package explore_with_me.mapper;

import org.mapstruct.Mapper;
import explore_with_me.dto.EndpointHitDtoOutput;
import explore_with_me.model.EndpointHit;

@Mapper
public interface EndpointHitMapper {
    EndpointHitDtoOutput endPointHitDtoOutputFromEndpointHit(EndpointHit endpointHit);
}
