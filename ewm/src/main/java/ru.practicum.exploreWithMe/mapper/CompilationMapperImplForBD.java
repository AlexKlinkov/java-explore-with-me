package ru.practicum.exploreWithMe.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.CompilationDtoOutput;
import ru.practicum.exploreWithMe.dto.CompilationDtoOutputForAdmin;
import ru.practicum.exploreWithMe.dto.NewCompilationDTOInput;
import ru.practicum.exploreWithMe.model.Compilation;

@Data
@Slf4j
@Component("CompilationMapperImplForBD")
@RequiredArgsConstructor
public class CompilationMapperImplForBD implements CompilationMapper{
    @Override
    public Compilation compilationFromNewCompilationDTOInput(NewCompilationDTOInput newCompilationDTOInput) {
        return null;
    }

    @Override
    public CompilationDtoOutput compilationDtoOutputFromCompilation(Compilation compilation) {
        return null;
    }

    @Override
    public CompilationDtoOutputForAdmin compilationDtoOutputForAdminFromCompilation(Compilation compilation) {
        return null;
    }
}
