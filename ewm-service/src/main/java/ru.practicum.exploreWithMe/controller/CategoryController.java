package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.CategoryDtoOutput;
import ru.practicum.exploreWithMe.dto.NewCategoryDTOInput;
import ru.practicum.exploreWithMe.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    // These endpoints for only Admins
    @Autowired
    @Qualifier("CategoryServiceInBD")
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDtoOutput> get(@RequestParam(value = "from", required = false, defaultValue = "0")
                                           Long from,
                                       @RequestParam(value = "size", required = false, defaultValue = "10")
                                           Long size) { // For every user (PUBLIC)
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDtoOutput getById(@PathVariable Long catId) { // For every user (PUBLIC)
        return categoryService.getCategoryById(catId);
    }

    @PatchMapping("/admin/categories")
    public CategoryDtoOutput update(@Valid @RequestBody NewCategoryDTOInput newCategoryDTOInput) { // For only ADMIN
        return categoryService.updateCategory(newCategoryDTOInput);
    }

    @PostMapping("/admin/categories")
    public CategoryDtoOutput create(@Valid @RequestBody NewCategoryDTOInput newCategoryDTOInput) { // For only ADMIN
        return categoryService.createCategory(newCategoryDTOInput);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void delete(@PathVariable Long catId) { // For only ADMIN
        categoryService.deleteCategoryById(catId);
    }
}

