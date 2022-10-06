package ru.practicum.exploreWithMe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.exploreWithMe.auxiliaryObjects.StatusOfParticipationRequest;
import ru.practicum.exploreWithMe.dto.ParticipationRequestDtoOutput;
import ru.practicum.exploreWithMe.mapper.ParticipationRequestMapperImplForBD;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.ParticipationRequest;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.ParticipationRequestRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;
import ru.practicum.exploreWithMe.service.ParticipationRequestServiceInDB;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ParticipationRequestServiceTest {
    @InjectMocks
    private ParticipationRequestServiceInDB participationRequestServiceInDB;
    @Mock
    private ParticipationRequestRepository participationRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private ParticipationRequestMapperImplForBD participationRequestMapper;
    private ParticipationRequestDtoOutput participationRequestDtoOutput;
    private ParticipationRequest participationRequest;
    private Event event;
    private User user;

    @BeforeEach
    public void init() {
        event = new Event();
        event.setId(1L);
        event.setRequestModeration(true);
        user = new User();
        user.setId(1L);
        LocalDateTime now = LocalDateTime.now();
        participationRequestDtoOutput = new ParticipationRequestDtoOutput(1L, event.getId(), now, user.getId(),
                StatusOfParticipationRequest.PENDING);
        participationRequest = new ParticipationRequest();
        participationRequest.setId(1L);
    }

    @Test
    public void getRequestPrivateTest() {
        List<ParticipationRequest> requests = new ArrayList<>();
        requests.add(participationRequest);
        when(participationRequestRepository.getAllByRequestorId(anyLong())).thenReturn(requests);
        participationRequestServiceInDB.getRequestPrivate(1L);
        Assertions.assertEquals(1L, participationRequestRepository.getAllByRequestorId(1L).get(0).getId());
    }

/*
    @Test
    public void createRequestPrivateTest() {
        when(participationRequestRepository.save(participationRequest)).thenReturn(participationRequest);
        when(eventRepository.getReferenceById(1L)).thenReturn(event);
        //doReturn(true).when(eventRepository.getReferenceById(1L)).getRequestModeration();
        participationRequestServiceInDB.createRequestPrivate(1L, event.getId());
        Assertions.assertEquals(1L, participationRequestRepository.save(participationRequest).getId());
        Assertions.assertEquals(1L, eventRepository.getReferenceById(1L).getId());
    }
*/

    @Test
    public void cancelOwnRequestPrivateTest() {
        participationRequest.setStatus(StatusOfParticipationRequest.REJECTED);
        when(participationRequestRepository.getByIdAndRequestorId(anyLong(), anyLong()))
                .thenReturn(participationRequest);
        participationRequestServiceInDB.cancelOwnRequestPrivate(1L, 1L);
        Assertions.assertEquals(StatusOfParticipationRequest.REJECTED,
                participationRequestRepository.getByIdAndRequestorId(1L, 1L).getStatus());
    }
}
