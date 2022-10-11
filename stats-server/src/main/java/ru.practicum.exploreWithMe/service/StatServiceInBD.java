package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.auxiliaryClasses.Attributes;
import ru.practicum.exploreWithMe.auxiliaryClasses.Converter;
import ru.practicum.exploreWithMe.dto.EndpointHitDTOInput;
import ru.practicum.exploreWithMe.dto.EndpointHitDtoOutput;
import ru.practicum.exploreWithMe.dto.StatOutput;
import ru.practicum.exploreWithMe.mapper.EndpointHitMapper;
import ru.practicum.exploreWithMe.mapper.EndpointHitMapperForBD;
import ru.practicum.exploreWithMe.model.EndpointHit;
import ru.practicum.exploreWithMe.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Component("StatServiceInBD")
public class StatServiceInBD implements StatService {
    @Autowired
    private final EndpointHitRepository endpointHitRepository;
    @Autowired
    @Qualifier("EndpointHitMapperForBD")
    final EndpointHitMapper endpointHitMapper;
    @Autowired
    private final Converter converter;

    @Override
    public EndpointHitDtoOutput saveStat(EndpointHitDTOInput endpointHitDTOInput) {
        log.debug("Save stat information by path : '/hit'");
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(endpointHitDTOInput.getApp());
        endpointHit.setAttributes(new Attributes(endpointHitDTOInput.getUri(), endpointHitDTOInput.getIp()));
        endpointHit.setTimestamp(converter.ConverterStringToLocalDataTime(endpointHitDTOInput.getTimestamp()));
        EndpointHit endpointHitForReturn = endpointHitRepository.save(endpointHit);
        return endpointHitMapper.endPointHitDtoOutputFromEndpointHit(endpointHitForReturn);

    }

    @Override
    public List<StatOutput> getStats(String start, String end, List<String> uris, Boolean unique) {
        log.debug("Get list with stat by path : '/stats'");
        LocalDateTime now = LocalDateTime.now();
        // 1. Получаем всю статистику
        List<EndpointHit> endpointHits = endpointHitRepository.findAll();
        List<EndpointHit> endpointHitsForReturn = endpointHits;
        // 2. Укарачиваем список с помощью параметров 'start' и 'end'
        if (start.length() > 0 && end.length() > 0) {
            endpointHitsForReturn = endpointHits.stream()
                    .filter(d -> d.getTimestamp().isAfter(converter.ConverterStringToLocalDataTime(start)) &&
                            d.getTimestamp().isBefore(converter.ConverterStringToLocalDataTime(end)))
                    .collect(Collectors.toList());
        }
        if (start.length() < 1 && end.length() > 0) {
            endpointHitsForReturn = endpointHits.stream()
                    .filter(d -> d.getTimestamp().isAfter(now) &&
                            d.getTimestamp().isBefore(converter.ConverterStringToLocalDataTime(end)))
                    .collect(Collectors.toList());
        }
        if (end.length() < 1 && start.length() < 1) {
            endpointHitsForReturn = endpointHits.stream()
                    .filter(d -> d.getTimestamp().isAfter(now))
                    .collect(Collectors.toList());
        }
        if (start.length() > 0 && end.length() < 1) {
            endpointHitsForReturn = endpointHits.stream()
                    .filter(d -> d.getTimestamp().isAfter(converter.ConverterStringToLocalDataTime(start)))
                    .collect(Collectors.toList());
        }
        // 3. Укарачиваем еще список спомощью параметра 'uris'
        if (!uris.isEmpty()) {
            List<EndpointHit> onlyByWithUris = endpointHitsForReturn.stream()
                    .filter(u -> uris.contains(u.getAttributes().getUri()))
                    .collect(Collectors.toList());
            if (!onlyByWithUris.isEmpty()) {
                endpointHitsForReturn = onlyByWithUris;
            }
        }
        // 4. Подготавливаем список для возврата
        List<StatOutput> statOutputList = new ArrayList<>();
        for (EndpointHit endpointHit : endpointHitsForReturn) {
            List<EndpointHit> allRequestWithThisApp = endpointHitRepository.findAllByAppIs(endpointHit.getApp());
            if (!unique) {
                statOutputList.add(new StatOutput(endpointHit.getApp(), endpointHit.getAttributes().getUri(),
                        allRequestWithThisApp.stream()
                                .filter(u -> u.getAttributes().getUri().equals(endpointHit.getAttributes().getUri()))
                                .count()
                ));
            } else {
                statOutputList.add(new StatOutput(endpointHit.getApp(), endpointHit.getAttributes().getUri(),
                        allRequestWithThisApp.stream()
                                .filter(ui -> Collections.frequency(allRequestWithThisApp, ui.getAttributes().getIp())
                                        < 1 &&
                                        ui.getAttributes().getUri().equals(endpointHit.getAttributes().getUri())
                                        && ui.getAttributes().getIp().equals(endpointHit.getAttributes().getIp()))
                                .count()));
            }
        }
        statOutputList = statOutputList.stream()
                .distinct()
                .collect(Collectors.toList());
        return statOutputList;
    }
}
