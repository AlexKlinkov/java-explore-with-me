/*
package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.CompilationDtoOutput;
import ru.practicum.exploreWithMe.dto.CompilationDtoOutputForAdmin;
import ru.practicum.exploreWithMe.dto.NewCompilationDTOInput;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompilationController {

    @GetMapping("/compilations")
    public List<CompilationDtoOutput> getCompilationsPublic (@RequestParam(name = "pinned", required = false,
                                                             defaultValue = "false") Boolean pinned,
                                                             @RequestParam(name = "from", required = false,
                                                             defaultValue = "0") Long from,
                                                             @RequestParam(name = "size", required = false,
                                                             defaultValue = "10") Long size) {

    }

    @GetMapping("/compilation/{compId}")
    public CompilationDtoOutput getCompilationByIdPublic (@PathVariable Long compId) {

    }

    @PostMapping("/admin/compilations")
    public CompilationDtoOutputForAdmin createCompilationsByAdmin (@RequestBody @Valid NewCompilationDTOInput
                                                                       newCompilationDTOInput) {

    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilationByIdAdmin (@PathVariable Long compId) {

    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilationByIdAdmin(@PathVariable Long compId, @PathVariable Long eventId) {

    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public CompilationDtoOutput addEventToCompilationAdmin(@PathVariable Long compId, @PathVariable Long eventId) {

    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void unPinCompilationFromMainPageAdmin (@PathVariable Long compId) {

    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void PinCompilationOnMainPageAdmin (@PathVariable Long compId) {

    }
}
*/
