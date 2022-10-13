package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.EndpointHitDTOInput;
import ru.practicum.exploreWithMe.dto.StatOutput;
import ru.practicum.exploreWithMe.service.StatService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {
    @Autowired
    @Qualifier("StatServiceInBD")
    private final StatService statService;

    @PostMapping("/hit")
    public void saveStat(@RequestBody @Valid EndpointHitDTOInput endpointHitDTOInput){
        statService.saveStat(endpointHitDTOInput);
    }

    @GetMapping("/stats")
    public List<StatOutput> getStats (@RequestParam(name = "start", required = false) String start,
                                      @RequestParam(name = "end", required = false) String end,
                                      @RequestParam(name = "uris", required = false) List<String> uris,
                                      @RequestParam(name = "unique", required = false, defaultValue = "false")
                                                    Boolean unique) {
        return statService.getStats(start, end, uris, unique);
    }
}
