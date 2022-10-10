package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.EndpointHitDTOInput;
import ru.practicum.exploreWithMe.dto.EndpointHitDtoOutput;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Component("StatServiceInBD")
public class StatServiceInBD implements StatService{
    @Override
    public EndpointHitDtoOutput saveStat(EndpointHitDTOInput endpointHitDTOInput) {
        return null;
    }

    @Override
    public List<EndpointHitDtoOutput> getStats(String start, String End, List<String> uris, Boolean unique) {
        return null;
    }
}
