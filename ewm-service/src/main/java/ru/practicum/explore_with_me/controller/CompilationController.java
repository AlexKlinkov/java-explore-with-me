package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.CompilationDtoOutput;
import ru.practicum.explore_with_me.dto.NewCompilationDTOInput;
import ru.practicum.explore_with_me.service.CompilationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompilationController {
    @Autowired
    @Qualifier("CompilationServiceInBD")
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDtoOutput> getCompilationsPublic (@RequestParam(name = "pinned", required = false,
                                                             defaultValue = "false") Boolean pinned,
                                                             @RequestParam(name = "from", required = false,
                                                             defaultValue = "0") Long from,
                                                             @RequestParam(name = "size", required = false,
                                                             defaultValue = "10") Long size) {
        System.out.println("here");
        return compilationService.getCompilationsPublic(pinned, from, size);

    }

    @GetMapping("/compilations/{compId}")
    public CompilationDtoOutput getCompilationByIdPublic (@PathVariable Long compId) {
        return compilationService.getCompilationByIdPublic(compId);
    }

    @PostMapping("/admin/compilations")
    public CompilationDtoOutput createCompilationsByAdmin (@RequestBody @Valid NewCompilationDTOInput
                                                                       newCompilationDTOInput) {
        return compilationService.createCompilationsByAdmin(newCompilationDTOInput);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilationByIdAdmin (@PathVariable Long compId) {
        compilationService.deleteCompilationByIdAdmin(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilationByIdAdmin(@PathVariable Long compId, @PathVariable Long eventId) {
        compilationService.deleteEventFromCompilationByIdAdmin(compId, eventId);
    }
    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public CompilationDtoOutput addEventToCompilationAdmin(@PathVariable Long compId, @PathVariable Long eventId) {
        return compilationService.addEventToCompilationAdmin(compId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void unPinCompilationFromMainPageAdmin (@PathVariable Long compId) {
        compilationService.unPinCompilationFromMainPageAdmin(compId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public void pinCompilationOnMainPageAdmin (@PathVariable Long compId) {
        compilationService.pinCompilationOnMainPageAdmin(compId);
    }
}
