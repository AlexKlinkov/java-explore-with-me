package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.CategoryDtoOutput;
import ru.practicum.exploreWithMe.dto.NewCategoryDTOInput;
import ru.practicum.exploreWithMe.mapper.CategoryMapper;
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
    public List<CategoryDtoOutput> getCategories(Long from, Long size) { // GET /categories
        log.debug("Get categories by path '/categories'");
        return categoryRepository.getAll().stream()
                .map((category) -> categoryMapper.categoryDtoOutputFromCategory(category))
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDtoOutput getCategoryById(Long categoryId) { // GET /categories/{catId}
        log.debug("Get category by path '/categories/{catId}'");
        return categoryMapper.categoryDtoOutputFromCategory(categoryRepository.getReferenceById(categoryId));
    }

    // PATCH admin/categories
    @Override
    public CategoryDtoOutput updateCategory(NewCategoryDTOInput newCategoryDTOInput) {
        log.debug("Update category by path 'admin/categories'");
        categoryRepository.update(newCategoryDTOInput.getId(), newCategoryDTOInput.getName());
        return categoryMapper.categoryDtoOutputFromCategory(
                categoryRepository.getReferenceById(newCategoryDTOInput.getId())
        );
    }

    @Override
    public CategoryDtoOutput createCategory(NewCategoryDTOInput newCategoryDTOInput) { // POST admin/categories
        log.debug("Create category by path 'admin/categories'");
        return categoryMapper.categoryDtoOutputFromCategory(
                categoryRepository.save(categoryMapper.categoryFromNewCategoryDTOInput(newCategoryDTOInput))
        );
    }

    @Override
    public void deleteCategoryById(Long categoryId) { // DELETE admin/categories/{catId}
        log.debug("Delete category by path 'admin/categories'");
        categoryRepository.deleteById(categoryId);
    }
}
