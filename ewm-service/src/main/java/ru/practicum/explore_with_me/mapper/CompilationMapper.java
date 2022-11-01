package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.CompilationDtoOutput;
import ru.practicum.explore_with_me.model.Compilation;

@Mapper
public interface CompilationMapper {
    CompilationDtoOutput compilationDtoOutputFromCompilation(Compilation compilation);
}
