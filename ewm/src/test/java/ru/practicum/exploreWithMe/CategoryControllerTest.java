package ru.practicum.exploreWithMe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.exploreWithMe.controller.CategoryController;
import ru.practicum.exploreWithMe.dto.CategoryDtoOutput;
import ru.practicum.exploreWithMe.dto.NewCategoryDTOInput;
import ru.practicum.exploreWithMe.service.CategoryServiceInBD;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private CategoryServiceInBD categoryServiceInBD;
    private MockMvc mvc;
    private NewCategoryDTOInput categoryDTOInput;
    private CategoryDtoOutput categoryDtoOutput;
    @BeforeEach
    public void init () {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        categoryDTOInput = new NewCategoryDTOInput(null, "Concerts");
        categoryDtoOutput = new CategoryDtoOutput((long)1, "Concerts");
    }

    @Test
    public void getCategoryTestById() throws Exception {
        when(categoryServiceInBD.getCategoryById(anyLong()))
                .thenReturn(categoryDtoOutput);

        mvc.perform(get("/categories/1")
                        .content(mapper.writeValueAsString(categoryDTOInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(categoryDtoOutput.getName())));
    }

    @Test
    public void getCategoriesTest() throws Exception {
        when(categoryServiceInBD.getCategories(anyLong(), anyLong()))
                .thenReturn(List.of(categoryDtoOutput));

        mvc.perform(get("/categories")
                        .content(mapper.writeValueAsString(categoryDTOInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is(categoryDtoOutput.getName())));
    }

    @Test
    public void updateCategoryTest() throws Exception {
        when(categoryServiceInBD.updateCategory(categoryDTOInput))
                .thenReturn(categoryDtoOutput);

        mvc.perform(patch("/admin/categories")
                        .content(mapper.writeValueAsString(categoryDTOInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(categoryDtoOutput.getName())));
    }

    @Test
    public void createCategoryTest() throws Exception {
        when(categoryServiceInBD.createCategory(categoryDTOInput))
                .thenReturn(categoryDtoOutput);

        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(categoryDTOInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(categoryDtoOutput.getName())));
    }

    @Test
    public void deleteCategoryByIdTest() throws Exception {
        doNothing().when(categoryServiceInBD).deleteCategoryById(anyLong());
        mvc.perform(delete("/admin/categories/1")
                        .content(mapper.writeValueAsString(categoryDTOInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
