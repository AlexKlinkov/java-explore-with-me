package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.EndpointHitDTOInput;
import ru.practicum.exploreWithMe.dto.EndpointHitDtoOutput;

import java.util.List;

public interface StatService {
    EndpointHitDtoOutput saveStat (EndpointHitDTOInput endpointHitDTOInput);
    List<EndpointHitDtoOutput> getStats (String start, String End, List<String> uris, Boolean unique);
}
