package explore_with_me.service;

import explore_with_me.dto.EndpointHitDTOInput;
import explore_with_me.dto.StatOutput;

import java.util.List;

public interface StatService {
    void saveStat (EndpointHitDTOInput endpointHitDTOInput);
    List<StatOutput> getStats (String start, String end, List<String> uris, Boolean unique);
}
