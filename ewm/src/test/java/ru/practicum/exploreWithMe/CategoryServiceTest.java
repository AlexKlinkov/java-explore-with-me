package ru.practicum.exploreWithMe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.exploreWithMe.dto.CategoryDtoOutput;
import ru.practicum.exploreWithMe.dto.NewCategoryDTOInput;
import ru.practicum.exploreWithMe.mapper.CategoryMapperImplForBD;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.repository.CategoryRepository;
import ru.practicum.exploreWithMe.service.CategoryServiceInBD;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceInBD categoryServiceInBD;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapperImplForBD categoryMapperImplForBD;
    NewCategoryDTOInput categoryDTOInput;
    CategoryDtoOutput categoryDtoOutput;
    Category category;

    @BeforeEach
    public void init() {
        categoryDTOInput = new NewCategoryDTOInput(null, "Concerts");
        categoryDtoOutput = new CategoryDtoOutput(1L, "Concerts");
        category = new Category(1L, "Concerts");
    }

    @Test
    public void getCategoriesTestFrom0Size1() {
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        when(categoryRepository.getAll()).thenReturn(categories);
        categoryServiceInBD.getCategories( 0L, 1L);
        Assertions.assertEquals(category.getId(), categoryRepository.getAll().get(0).getId());
    }

    @Test
    public void getCategoryByIdTest() {
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        categoryServiceInBD.getCategoryById( 1L);
        Assertions.assertEquals(category.getId(), categoryRepository.getReferenceById(1L).getId());
    }

    @Test
    public void updateCategoryTest() {
        when(categoryRepository.save(category)).thenReturn(category);
        categoryServiceInBD.updateCategory(categoryDTOInput);
        Assertions.assertEquals(categoryRepository.save(category).getName(), category.getName());
    }

    @Test
    public void createCategoryTest() {
        when(categoryRepository.save(category)).thenReturn(category);
        categoryServiceInBD.createCategory( categoryDTOInput);
        Assertions.assertEquals(category.getId(), categoryRepository.save(category).getId());
    }

    @Test
    public void deleteUserTest() {
        // CategoryRepository is just a mock
        CategoryRepository categoryRepositoryMock = Mockito.mock(CategoryRepository.class);
        // Creating the real CategoryServiceInBD
        CategoryServiceInBD categoryServiceInBDReal = new CategoryServiceInBD(categoryRepositoryMock);
        // Creating a spy proxy
        CategoryServiceInBD spy = spy(categoryServiceInBDReal);
        spy.deleteCategoryById(category.getId());
        verify(categoryRepositoryMock, times(1)).deleteById(category.getId());
    }
}
