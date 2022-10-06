package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.CompilationDtoOutput;
import ru.practicum.exploreWithMe.dto.CompilationDtoOutputForAdmin;
import ru.practicum.exploreWithMe.dto.NewCompilationDTOInput;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Component("CompilationServiceInBD")
public class CompilationServiceInBD implements CompilationService{
    @Override
    public List<CompilationDtoOutput> getCompilationsPublic(Boolean pinned, Long from, Long size) {
        return null;
    }

    @Override
    public CompilationDtoOutput getCompilationByIdPublic(Long compId) {
        return null;
    }

    @Override
    public CompilationDtoOutputForAdmin createCompilationsByAdmin(NewCompilationDTOInput newCompilationDTOInput) {
        return null;
    }

    @Override
    public void deleteCompilationByIdAdmin(Long compId) {

    }

    @Override
    public void deleteEventFromCompilationByIdAdmin(Long compId, Long eventId) {

    }

    @Override
    public CompilationDtoOutput addEventToCompilationAdmin(Long compId, Long eventId) {
        return null;
    }

    @Override
    public void unPinCompilationFromMainPageAdmin(Long compId) {

    }

    @Override
    public CompilationDtoOutput PinCompilationOnMainPageAdmin(Long compId) {
        return null;
    }
}
