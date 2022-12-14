package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.auxiliary_objects.CompilationCheckValidationMethods;
import ru.practicum.explore_with_me.dto.CompilationDtoOutput;
import ru.practicum.explore_with_me.dto.NewCompilationDTOInput;
import ru.practicum.explore_with_me.exeption.*;
import ru.practicum.explore_with_me.mapper.CompilationMapper;
import ru.practicum.explore_with_me.model.Compilation;
import ru.practicum.explore_with_me.model.CompilationEvents;
import ru.practicum.explore_with_me.repository.CompilationEventsRepository;
import ru.practicum.explore_with_me.repository.CompilationRepository;
import ru.practicum.explore_with_me.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Component("CompilationServiceInBD")
public class CompilationServiceInBD implements CompilationService {
    @Autowired
    private final CompilationRepository compilationRepository;
    @Autowired
    private final CompilationEventsRepository compilationEventsRepository;
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    @Qualifier("CompilationMapperImplForBD")
    final CompilationMapper compilationMapper;

    @Override
    public List<CompilationDtoOutput> getCompilationsPublic(Boolean pinned, Long from, Long size) {
        log.debug("Get compilation (PUBLIC) by path : '/compilations'");
        CompilationCheckValidationMethods.checkParamsOfPageFromAndSize(from, size);
        // 1. Получаем полный список по параметру pinned из таблицы compilation
        List<Compilation> compilations = compilationRepository.getAllByPinnedIs(pinned);
        // 2. Получившийся список укарачиваем с помощью параметров from и size и возвращаем пользователю
        return compilations.stream()
                .map(compilationMapper::compilationDtoOutputFromCompilation)
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());

    }

    @Override
    public CompilationDtoOutput getCompilationByIdPublic(Long compId) {
        log.debug("Get compilation by id (PUBLIC) follow path : '/compilations/compId'");
        CompilationCheckValidationMethods.checkParamOfId(compId, "CompilationId");
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found.");
        }
        Compilation compilation = compilationRepository.getReferenceById(compId);
        return compilationMapper.compilationDtoOutputFromCompilation(compilation);
    }

    @Override
    public CompilationDtoOutput createCompilationsByAdmin(NewCompilationDTOInput newCompilationDTOInput) {
        log.debug("Create compilations by Admin using path : '/admin/compilations'");
        // 1. Compilation table
        Compilation compilation = new Compilation();
        compilation.setPinned(newCompilationDTOInput.getPinned());
        compilation.setTitle(newCompilationDTOInput.getTitle());
        Compilation compilationWithId = compilationRepository.save(compilation);
        // 2. CompilationEvents table
        CompilationEvents compilationEvents = new CompilationEvents();
        // 2.1 Take all eventId from newCompilationDTOInput and put this data in the table
        List<Long> eventIds = newCompilationDTOInput.getEventsId();
        for (Long id : eventIds) {
            compilationEvents.setCompilationId(compilationWithId.getId());
            compilationEvents.setEventId(id);
            compilationEventsRepository.save(compilationEvents);
        }
        return compilationMapper.compilationDtoOutputFromCompilation(compilation);
    }

    @Override
    public void deleteCompilationByIdAdmin(Long compId) {
        log.debug("Delete compilation by id (ADMIN) follow path : '/admin/compilations/{compId}'");
        CompilationCheckValidationMethods.checkParamOfId(compId, "CompilationId");
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found.");
        }
        // 1. Удаляем записи из промежуточной таблицы 'compilation_events'
        compilationEventsRepository.deleteAllByCompilationId(compId);
        // 2. Удаляем запись из самой таблицы 'compilation'
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventFromCompilationByIdAdmin(Long compId, Long eventId) {
        log.debug("Delete event from compilation by compilationId (ADMIN) " +
                "follow path : '/admin/compilations/{compId}/events/{eventId}'");
        CompilationCheckValidationMethods.checkParamOfId(eventId, "EventId");
        if (!eventRepository.existsById(compId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        CompilationCheckValidationMethods.checkParamOfId(compId, "CompilationId");
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found.");
        }
        if (compilationEventsRepository.getByCompilationIdAndAndEventId(compId, eventId) == null) {
            throw new ConflictException("Event with id=" + eventId + " is not located in compilation with id=" + compId);
        }
        // 1. Удаляем записи из промежуточной таблицы 'compilation_events'
        compilationEventsRepository.deleteAllByCompilationIdAndEventId(compId, eventId);
        // 2. Удаляем запись из самой таблицы 'compilation'
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDtoOutput addEventToCompilationAdmin(Long compId, Long eventId) {
        log.debug("Add event to compilation (ADMIN) " +
                "follow path : '/admin/compilations/{compId}/events/{eventId}'");
        CompilationCheckValidationMethods.checkParamOfId(compId, "CompilationId");
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found.");
        }
        CompilationCheckValidationMethods.checkParamOfId(eventId, "EventId");
        if (!eventRepository.existsById(compId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        List<CompilationEvents> compilationEventsList = compilationEventsRepository.findAll();
        boolean isError = false;
        for (CompilationEvents compilationEvents : compilationEventsList) {
            if (compilationEvents.getCompilationId().equals(compId) &&
                    compilationEvents.getEventId().equals(eventId)) {
                isError = true;
                break;
            }
        }
        if (isError) {
            throw new ConflictException("Compilation with id=" + compId + " already has event with id=" + eventId);
        }
        // 1. Добавляем событие в подборку (в таблицу 'compilation_events')
        CompilationEvents newCompilationEvent = new CompilationEvents();
        newCompilationEvent.setCompilationId(compId);
        newCompilationEvent.setEventId(eventId);
        compilationEventsRepository.save(newCompilationEvent);
        return compilationMapper.compilationDtoOutputFromCompilation(compilationRepository.getReferenceById(compId));
    }

    @Override
    public void unPinCompilationFromMainPageAdmin(Long compId) {
        log.debug("Unpin compilation from main page " +
                "follow path : '/admin/compilations/{compId}/pin'");
        CompilationCheckValidationMethods.checkParamOfId(compId, "CompilationId");
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found.");
        }
        Compilation compilation = compilationRepository.getReferenceById(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilationOnMainPageAdmin(Long compId) {
        log.debug("Fix compilation on main page " +
                "follow path : '/admin/compilations/{compId}/pin'");
        CompilationCheckValidationMethods.checkParamOfId(compId, "CompilationId");
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found.");
        }
        Compilation compilation = compilationRepository.getReferenceById(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }
}
