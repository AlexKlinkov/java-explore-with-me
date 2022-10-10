package ru.practicum.exploreWithMe.mapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.ParticipationRequestDtoOutput;
import ru.practicum.exploreWithMe.model.ParticipationRequest;

@Data
@Slf4j
@Component("ParticipationRequestMapperImplForBD")
public class ParticipationRequestMapperImplForBD implements ParticipationRequestMapper {
    @Override
    public ParticipationRequestDtoOutput RequestDtoOutputFromParticipationRequest(
            ParticipationRequest participationRequest) {
        if (participationRequest == null) {
            return null;
        }

        ParticipationRequestDtoOutput participationRequestDtoOutput = new ParticipationRequestDtoOutput();
        participationRequestDtoOutput.setId(participationRequest.getId());
        participationRequestDtoOutput.setRequesterId(participationRequest.getRequestor().getId());
        participationRequestDtoOutput.setEventId(participationRequest.getEvent().getId());
        participationRequestDtoOutput.setCreated(participationRequest.getCreated());
        participationRequestDtoOutput.setStatus(participationRequest.getStatus());
        return participationRequestDtoOutput;
    }
}
