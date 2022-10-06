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
import ru.practicum.exploreWithMe.controller.CommentController;
import ru.practicum.exploreWithMe.dto.*;
import ru.practicum.exploreWithMe.service.CommentServiceInBD;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @InjectMocks
    private CommentController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private CommentServiceInBD commentServiceInBD;
    private MockMvc mvc;
    private NewCommentDTOInput newCommentDTOInput;
    private CommentDtoOutput commentDtoOutput;
    @BeforeEach
    public void init () {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        newCommentDTOInput = new NewCommentDTOInput(1L, "New Comment");
        commentDtoOutput = new CommentDtoOutput(1L, 1L, "Vasy",
                "New Comment", LocalDateTime.now());
    }

    @Test
    public void createCommentTest() throws Exception {
        when(commentServiceInBD.addCommentToEvent(any(), any(), any()))
                .thenReturn(commentDtoOutput);

        mvc.perform(post("/events/1/comments")
                        .content(mapper.writeValueAsString(newCommentDTOInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDtoOutput.getId().intValue())));
    }

    @Test
    public void deleteCommentByIdTest() throws Exception {
        doNothing().when(commentServiceInBD).deleteComment(anyLong(), anyLong());
        mvc.perform(delete("/events/comments/1")
                        .content(mapper.writeValueAsString(newCommentDTOInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCommentTest() throws Exception {
        when(commentServiceInBD.updateComment(any(), any(), any()))
                .thenReturn(commentDtoOutput);

        mvc.perform(patch("/events/comments/1")
                        .content(mapper.writeValueAsString(newCommentDTOInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDtoOutput.getId().intValue())));
    }

    @Test
    public void getCommentsTest() throws Exception {
        when(commentServiceInBD.getCommentsAboutEventById(anyLong()))
                .thenReturn(List.of(commentDtoOutput));

        mvc.perform(get("/events/1/comments")
                        .content(mapper.writeValueAsString(newCommentDTOInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(commentDtoOutput.getId().intValue())));
    }
}
