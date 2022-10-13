package ru.practicum.exploreWithMe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.exploreWithMe.auxiliary_objects.StatusOfParticipationRequest;
import ru.practicum.exploreWithMe.controller.ParticipationRequestController;
import ru.practicum.exploreWithMe.dto.ParticipationRequestDtoOutput;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.ParticipationRequest;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.service.ParticipationRequestServiceInDB;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ParticipationRequestControllerTest {
    @InjectMocks
    private ParticipationRequestController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private ParticipationRequestServiceInDB participationRequestServiceInDB;
    private MockMvc mvc;
    private ParticipationRequest participationRequest;
    private ParticipationRequestDtoOutput participationRequestDtoOutput;
    @BeforeEach
    public void init () {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LocalDateTime now = LocalDateTime.now();
        Event event = new Event();
        event.setId(1L);
        participationRequest = new ParticipationRequest(1L, event, now,
                new User(1L, "Mihail", "Mihail@mail.ru"), StatusOfParticipationRequest.PENDING);
        participationRequestDtoOutput = new ParticipationRequestDtoOutput(1L, event.getId(), now, 1L,
                StatusOfParticipationRequest.PENDING);
    }

    @Test
    public void getParticipationRequestTest() throws Exception {
        when(participationRequestServiceInDB.getRequestPrivate(any()))
                .thenReturn(List.of(participationRequestDtoOutput));

        mvc.perform(get("/users/1/requests")
                        .content(mapper.writeValueAsString(participationRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(participationRequestDtoOutput.getId().intValue())));
    }

    @Test
    public void createParticipationRequestTest() throws Exception {
        when(participationRequestServiceInDB.createRequestPrivate(anyLong(), anyLong()))
                .thenReturn(participationRequestDtoOutput);

        mvc.perform(post("/users/1/requests?eventId=1")
                        .content(mapper.writeValueAsString(participationRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(participationRequestDtoOutput.getId().intValue())));
    }

    @Test
    public void cancelParticipationRequestTest() throws Exception {
        when(participationRequestServiceInDB.cancelOwnRequestPrivate(anyLong(), anyLong()))
                .thenReturn(participationRequestDtoOutput);

        mvc.perform(patch("/users/1/requests/1/cancel")
                        .content(mapper.writeValueAsString(participationRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(participationRequestDtoOutput.getId().intValue())));
    }
}
