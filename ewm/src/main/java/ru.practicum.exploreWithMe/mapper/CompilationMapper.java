package ru.practicum.exploreWithMe.mapper;

import org.mapstruct.Mapper;
import ru.practicum.exploreWithMe.dto.CompilationDtoOutput;
import ru.practicum.exploreWithMe.dto.CompilationDtoOutputForAdmin;
import ru.practicum.exploreWithMe.dto.NewCompilationDTOInput;
import ru.practicum.exploreWithMe.model.Compilation;

@Mapper
public interface CompilationMapper {
    Compilation compilationFromNewCompilationDTOInput(NewCompilationDTOInput newCompilationDTOInput);
    CompilationDtoOutput compilationDtoOutputFromCompilation(Compilation compilation);
    CompilationDtoOutputForAdmin compilationDtoOutputForAdminFromCompilation(Compilation compilation);
}
