package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.CategoryDtoOutput;
import ru.practicum.explore_with_me.model.Category;

@Mapper
public interface CategoryMapper {
    CategoryDtoOutput categoryDtoOutputFromCategory(Category category);
}
