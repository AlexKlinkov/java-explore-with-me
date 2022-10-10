package ru.practicum.exploreWithMe.mapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.CategoryDtoOutput;
import ru.practicum.exploreWithMe.dto.NewCategoryDTOInput;
import ru.practicum.exploreWithMe.model.Category;

@Data
@Slf4j
@Component("CategoryMapperImplForBD")
public class CategoryMapperImplForBD implements CategoryMapper {
    @Override
    public Category categoryFromNewCategoryDTOInput(NewCategoryDTOInput newCategoryDTOInput) {
        if (newCategoryDTOInput == null) {
            return null;
        }
        Category category = new Category();
        category.setName(newCategoryDTOInput.getName());
        return category;
    }

    @Override
    public CategoryDtoOutput categoryDtoOutputFromCategory(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDtoOutput categoryDtoOutput = new CategoryDtoOutput();
        categoryDtoOutput.setId(category.getId());
        categoryDtoOutput.setName(category.getName());
        return categoryDtoOutput;
    }
}
