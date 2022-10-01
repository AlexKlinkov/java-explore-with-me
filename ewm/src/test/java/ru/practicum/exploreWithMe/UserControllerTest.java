package ru.practicum.exploreWithMe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.exploreWithMe.controller.UserController;
import ru.practicum.exploreWithMe.dto.UserDTOInput;
import ru.practicum.exploreWithMe.dto.UserDtoOutputForAdmin;
import ru.practicum.exploreWithMe.service.UserServiceInBD;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class UserControllerTest {

        @InjectMocks
        private UserController controller;
        private final ObjectMapper mapper = new ObjectMapper();
        @Mock
        private UserServiceInBD userService;
        private MockMvc mvc;
        private UserDTOInput userDTOInput;
        private UserDtoOutputForAdmin userDtoOutputForAdmin;
        @BeforeEach
        public void init () {
            mvc = MockMvcBuilders
                    .standaloneSetup(controller)
                    .build();

            userDTOInput = new UserDTOInput("Max", "max@created.ru");
            userDtoOutputForAdmin = new UserDtoOutputForAdmin(
                    1L,"Max", "max@created.ru"
            );
        }
        @Test
        public void createUserTest() throws Exception {
            when(userService.createUser(userDTOInput))
                    .thenReturn(userDtoOutputForAdmin);

            mvc.perform(post("/admin/users")
                            .content(mapper.writeValueAsString(userDTOInput))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(userDtoOutputForAdmin.getName())))
                    .andExpect(jsonPath("$.email", is(userDtoOutputForAdmin.getEmail())));
        }

    @Test
    public void getUsersTest() throws Exception {
        when(userService.getUsers(any(), anyLong(), anyLong()))
                .thenReturn(List.of(userDtoOutputForAdmin));

        mvc.perform(get("/admin/users")
                        .content(mapper.writeValueAsString(userDTOInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is(userDtoOutputForAdmin.getName())))
                .andExpect(jsonPath("$.[0].email", is(userDtoOutputForAdmin.getEmail())));
    }

    @Test
    public void deleteUsersByIdTest() throws Exception {
        mvc.perform(delete("/admin/users/1")
                        .content(mapper.writeValueAsString(userDTOInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
