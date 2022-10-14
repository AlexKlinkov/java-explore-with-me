package ru.practicum.explore_with_me.service;


import ru.practicum.explore_with_me.dto.CategoryDtoOutput;
import ru.practicum.explore_with_me.dto.NewCategoryDTOInput;

import java.util.List;

public interface CategoryService {

    // Work with entity 'Category' on change something can only admin, but get information about category can any user

    // ANY USER

    // This method return list of categories
    // with next params: 'from' (indicate amount of first elements which have to be skipped) and
    // 'size' (amount of elements in set)
    List<CategoryDtoOutput> getCategories (Long from, Long size); // GET /categories
    CategoryDtoOutput getCategoryById (Long categoryId); // GET /categories/{catId}

    // ONLY ADMIN

    // This DTO 'NewCategoryDTOInputForUpdate' contains also field 'id' while this one 'NewCategoryDTOInput' contains
    // only field 'name'
    // PATCH admin/categories
    CategoryDtoOutput updateCategory (NewCategoryDTOInput NewCategoryDTOInput);
    CategoryDtoOutput createCategory (NewCategoryDTOInput newCategoryDTOInput); // POST admin/categories
    void deleteCategoryById (Long categoryId); // DELETE admin/categories/{catId}
}
