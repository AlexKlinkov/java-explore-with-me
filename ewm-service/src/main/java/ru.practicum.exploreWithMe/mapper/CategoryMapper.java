package ru.practicum.exploreWithMe.mapper;

import org.mapstruct.Mapper;
import ru.practicum.exploreWithMe.dto.CategoryDtoOutput;
import ru.practicum.exploreWithMe.dto.NewCategoryDTOInput;
import ru.practicum.exploreWithMe.model.Category;

@Mapper
public interface CategoryMapper {
    Category categoryFromNewCategoryDTOInput(NewCategoryDTOInput newCategoryDTOInput);
    CategoryDtoOutput categoryDtoOutputFromCategory(Category category);
}
