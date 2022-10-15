package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.auxiliary_objects.CompilationCheckValidationMethods;
import ru.practicum.explore_with_me.dto.CategoryDtoOutput;
import ru.practicum.explore_with_me.dto.NewCategoryDTOInput;
import ru.practicum.explore_with_me.exeption.*;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.model.Category;
import ru.practicum.explore_with_me.repository.CategoryRepository;

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
        CompilationCheckValidationMethods.checkParamsOfPageFromAndSize(from, size);
        Pageable pageable = PageRequest.of(from.intValue(), size.intValue());
        return categoryRepository.findAllWithPagination(pageable).getContent().stream()
                .map((category) -> categoryMapper.categoryDtoOutputFromCategory(category))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDtoOutput getCategoryById(Long categoryId) {
        log.debug("Get category by path '/categories/{catId}'");
        CompilationCheckValidationMethods.checkParamOfId(categoryId, "CategoryId");
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Category with id=" + categoryId + " was not found.");
        }
        return categoryMapper.categoryDtoOutputFromCategory(categoryRepository.getReferenceById(categoryId));
    }

    @Override
    public CategoryDtoOutput updateCategory(NewCategoryDTOInput newCategoryDTOInput) {
        log.debug("Update category by path 'admin/categories'");
        if (newCategoryDTOInput.getId() == null || newCategoryDTOInput.getName() == null ||
                newCategoryDTOInput.getName().isEmpty()) {
            throw new NotCorrectArgumentsInMethodException("Category cannot be with id=null or with name=null/empty");
        }
        CompilationCheckValidationMethods.checkParamOfId(newCategoryDTOInput.getId(), "CategoryId");
        if (!categoryRepository.existsById(newCategoryDTOInput.getId())) {
            throw new NotFoundException("Category with id=" + newCategoryDTOInput.getId() + " was not found.");
        }
        categoryRepository.update(newCategoryDTOInput.getId(), newCategoryDTOInput.getName());
        return categoryMapper.categoryDtoOutputFromCategory(
                categoryRepository.getReferenceById(newCategoryDTOInput.getId())
        );
    }

    @Override
    public CategoryDtoOutput createCategory(NewCategoryDTOInput newCategoryDTOInput) { // POST admin/categories
        log.debug("Create category by path 'admin/categories'");
        if (newCategoryDTOInput.getName() == null || newCategoryDTOInput.getName().isEmpty()) {
            throw new NotCorrectArgumentsInMethodException("Category cannot be with name=null/empty");
        }
        Category category = new Category();
        category.setName(newCategoryDTOInput.getName());
        Category categoryForReturn = categoryRepository.save(category);
        return categoryMapper.categoryDtoOutputFromCategory(categoryForReturn);
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        log.debug("Delete category by path 'admin/categories'");
        CompilationCheckValidationMethods.checkParamOfId(categoryId, "CategoryId");
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Category with id=" + categoryId + " was not found.");
        }
        categoryRepository.deleteById(categoryId);
    }
}
