package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.EndpointHitDTOInput;
import ru.practicum.exploreWithMe.dto.EndpointHitDtoOutput;
import ru.practicum.exploreWithMe.dto.StatOutput;

import java.util.List;

public interface StatService {
    EndpointHitDtoOutput saveStat (EndpointHitDTOInput endpointHitDTOInput);
    List<StatOutput> getStats (String start, String End, List<String> uris, Boolean unique);
}
