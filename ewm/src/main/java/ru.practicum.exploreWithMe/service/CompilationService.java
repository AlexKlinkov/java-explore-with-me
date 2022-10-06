package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.CompilationDtoOutput;
import ru.practicum.exploreWithMe.dto.CompilationDtoOutputForAdmin;
import ru.practicum.exploreWithMe.dto.NewCompilationDTOInput;

import java.util.List;

public interface CompilationService {
    // param 'pinned' by default is 'false' indicates pinned this compilation on main page or not
    // param 'from' indicates amount first element from list which have to be skipped (default value is 0)
    // param 'size' indicates common amount in set (default value is 10)
    // GET /compilations
    List<CompilationDtoOutput> getCompilationsPublic(Boolean pinned, Long from, Long size);
    // GET /compilations/{compId}
    CompilationDtoOutput getCompilationByIdPublic(Long compId);
    // POST /admin/compilations
    CompilationDtoOutputForAdmin createCompilationsByAdmin(NewCompilationDTOInput newCompilationDTOInput);
    // DELETE /admin/compilations/{compId}
    void deleteCompilationByIdAdmin(Long compId);
    // DELETE /admin/compilations/{compId}/events/{eventId}
    void deleteEventFromCompilationByIdAdmin(Long compId, Long eventId);
    // PATCH /admin/compilations/{compId}/events/{eventId}
    CompilationDtoOutput addEventToCompilationAdmin(Long compId, Long eventId);
    // DELETE /admin/compilations/{compId}/pin
    void unPinCompilationFromMainPageAdmin(Long compId);
    // PATCH /admin/compilations/{compId}/pin
    CompilationDtoOutput PinCompilationOnMainPageAdmin(Long compId);
}
