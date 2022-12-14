package ru.practicum.explore_with_me.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.CompilationDtoOutput;
import ru.practicum.explore_with_me.dto.EventFullDtoOutput;
import ru.practicum.explore_with_me.model.Compilation;
import ru.practicum.explore_with_me.model.CompilationEvents;
import ru.practicum.explore_with_me.repository.CompilationEventsRepository;
import ru.practicum.explore_with_me.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Component("CompilationMapperImplForBD")
@RequiredArgsConstructor
public class CompilationMapperImplForBD implements CompilationMapper{
    @Autowired
    private final CompilationEventsRepository compilationEventsRepository;
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    @Qualifier("EventMapperImplForBD")
    private final EventMapper eventMapper;


    @Override
    public CompilationDtoOutput compilationDtoOutputFromCompilation(Compilation compilation) {

       if (compilation == null) {
           return null;
       }

       CompilationDtoOutput compilationDtoOutput = new CompilationDtoOutput();
       compilationDtoOutput.setId(compilation.getId());
       List<CompilationEvents> compilationEventsList =
               compilationEventsRepository.getAllByCompilationId(compilation.getId());
       if (compilationEventsList != null) {
           List<EventFullDtoOutput> events = new ArrayList<>();
           for (CompilationEvents compilationEvents : compilationEventsList) {
               events.add(eventMapper.eventFullDtoOutputFromEvent(
                       eventRepository.getReferenceById(compilationEvents.getEventId())));
           }
           compilationDtoOutput.setEvents(events);
       }
        compilationDtoOutput.setPinned(compilation.getPinned());
        compilationDtoOutput.setTitle(compilation.getTitle());
       return compilationDtoOutput;
    }
}
