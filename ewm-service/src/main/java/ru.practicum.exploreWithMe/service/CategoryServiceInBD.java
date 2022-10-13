package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.auxiliary_objects.CompilationCheckValidationMethods;
import ru.practicum.exploreWithMe.dto.CategoryDtoOutput;
import ru.practicum.exploreWithMe.dto.NewCategoryDTOInput;
import ru.practicum.exploreWithMe.exeption.*;
import ru.practicum.exploreWithMe.mapper.CategoryMapper;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Component("CategoryServiceInBD")
@RequiredArgsConstructor
public class CategoryServiceInBD implements CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    @Qualifier("CategoryMapperImplForBD")
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryDtoOutput> getCategories(Long from, Long size) {
        log.debug("Get categories by path '/categories'");
        if (CompilationCheckValidationMethods.checkParamsOfPageFromAndSize(from, size)) {
            throw new NotCorrectArgumentsInMethodException("Size cannot be less than 1, " +
                    "also from cannot be less then 0");
        }
        try {
            return categoryRepository.getAll().stream()
                    .map((category) -> categoryMapper.categoryDtoOutputFromCategory(category))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public CategoryDtoOutput getCategoryById(Long categoryId) {
        log.debug("Get category by path '/categories/{catId}'");
        if (CompilationCheckValidationMethods.checkParamOfId(categoryId)) {
            throw new ValidationException("Id of category cannot be less than 0");
        }
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Category with id=" + categoryId + " was not found.");
        }
        try {
            return categoryMapper.categoryDtoOutputFromCategory(categoryRepository.getReferenceById(categoryId));
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public CategoryDtoOutput updateCategory(NewCategoryDTOInput newCategoryDTOInput) {
        log.debug("Update category by path 'admin/categories'");
        if (newCategoryDTOInput.getId() == null || newCategoryDTOInput.getName() == null ||
                newCategoryDTOInput.getName().isEmpty()) {
            throw new NotCorrectArgumentsInMethodException("Category cannot be with id=null or with name=null/empty");
        }
        if (CompilationCheckValidationMethods.checkParamOfId(newCategoryDTOInput.getId())) {
            throw new ValidationException("Id of category cannot be less than 0");
        }
        if (!categoryRepository.existsById(newCategoryDTOInput.getId())) {
            throw new NotFoundException("Category with id=" + newCategoryDTOInput.getId() + " was not found.");
        }
        try {
            categoryRepository.update(newCategoryDTOInput.getId(), newCategoryDTOInput.getName());
            return categoryMapper.categoryDtoOutputFromCategory(
                    categoryRepository.getReferenceById(newCategoryDTOInput.getId())
            );
        } catch (ConflictException exception) {
            throw new ConflictException("Category with this name " + newCategoryDTOInput.getName() + " already exist");
        }
    }

    @Override
    public CategoryDtoOutput createCategory(NewCategoryDTOInput newCategoryDTOInput) { // POST admin/categories
        log.debug("Create category by path 'admin/categories'");
        if (newCategoryDTOInput.getName() == null || newCategoryDTOInput.getName().isEmpty()) {
            throw new NotCorrectArgumentsInMethodException("Category cannot be with name=null/empty");
        }
        try {
            Category category = new Category();
            category.setName(newCategoryDTOInput.getName());
            Category categoryForReturn = categoryRepository.save(category);
            return categoryMapper.categoryDtoOutputFromCategory(categoryForReturn);
        } catch (ConflictException exception) {
            throw new ConflictException("Category with this name " + newCategoryDTOInput.getName() + " already exist");
        }
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        log.debug("Delete category by path 'admin/categories'");
        if (CompilationCheckValidationMethods.checkParamOfId(categoryId)) {
            throw new ValidationException("Id of category cannot be less than 0");
        }
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Category with id=" + categoryId + " was not found.");
        }
        try {
            categoryRepository.deleteById(categoryId);
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }
}
