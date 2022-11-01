package ru.practicum.explore_with_me.mapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.CategoryDtoOutput;
import ru.practicum.explore_with_me.model.Category;


@Data
@Slf4j
@Component("CategoryMapperImplForBD")
public class CategoryMapperImplForBD implements CategoryMapper {
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
