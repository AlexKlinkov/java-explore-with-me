package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.auxiliaryObjects.Converter;
import ru.practicum.exploreWithMe.auxiliaryObjects.StatusOfEvent;
import ru.practicum.exploreWithMe.dto.EventFullDtoOutput;
import ru.practicum.exploreWithMe.dto.EventShortDtoOutput;
import ru.practicum.exploreWithMe.dto.NewEventDTOInput;
import ru.practicum.exploreWithMe.dto.ParticipationRequestDtoOutput;
import ru.practicum.exploreWithMe.mapper.EventMapper;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.repository.CategoryRepository;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Component("EventServiceInBD")
@RequiredArgsConstructor
public class EventServiceInBD implements EventService {
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    @Qualifier("EventMapperImplForBD")
    final EventMapper eventMapper;
    @Autowired
    private Converter converter;

    @Override
    public List<EventShortDtoOutput> getEventsPublic(String text, List<Long> categoryIds, Boolean paid,
                                                     String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                     String sortEventDateOrViews, Long from, Long size) {
        log.debug("Get list with Published events (PUBLIC) by path : '/events'");
        LocalDateTime now = LocalDateTime.now();
        // 1. Все опубликованные события
        List<Event> publishedEvents = eventRepository.getAllByStateIs(StatusOfEvent.PUBLISHED);
        // 2. Список для возврата событий
        List<Event> eventsForReturn = publishedEvents;
        // 3. Опубликованные события с учетом поискового текста
        if (text.length() > 0) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(t -> t.getAnnotation().toUpperCase().contains(text.toUpperCase()) ||
                            t.getDescription().toUpperCase().contains(text.toUpperCase()))
                    .collect(Collectors.toList());
        }
        // 4. Все события подходящие под список categoryId
        if (categoryIds.size() > 0) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(i -> categoryIds.contains(i.getId()))
                    .collect(Collectors.toList());
        }
        // 5. Следущая итерация, укарачиваем список с помощью параметра paid (по умолчанию = false)
        eventsForReturn = eventsForReturn.stream()
                .filter(p -> p.getPaid().equals(paid))
                .collect(Collectors.toList());
        // 6. Следущая итерация, укарачиваем список с помощью параметра диапазона дат
        if (rangeStart.length() > 0  && rangeEnd.length() > 0) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(d -> d.getEventDate().isAfter(converter.ConverterStringToLocalDataTime(rangeStart)) &&
                            d.getEventDate().isBefore(converter.ConverterStringToLocalDataTime(rangeEnd)))
                    .collect(Collectors.toList());
        }
        if (rangeStart.length() > 0  && rangeEnd.length() < 1) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(d -> d.getEventDate().isAfter(converter.ConverterStringToLocalDataTime(rangeStart)))
                    .collect(Collectors.toList());
        }
        if (rangeStart.length() < 1 && rangeEnd.length() > 0) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(d -> d.getEventDate().isAfter(now) &&
                            d.getEventDate().isBefore(converter.ConverterStringToLocalDataTime(rangeEnd)))
                    .collect(Collectors.toList());
        }
        if (rangeStart.length() < 1 && rangeEnd.length() < 1) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(d -> d.getEventDate().isAfter(now))
                    .collect(Collectors.toList());
        }
        // 7. Следущая итерация, укарачиваем список учитывая параметр доступности события (по умолчанию = false)
        // Только события у которых не исчерпан лимит запрос на участие
        if (!onlyAvailable) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(d -> d.getConfirmedRequests() < d.getParticipantLimit())
                    .collect(Collectors.toList());
        }
        // 8. Следущая итерация, укарачиваем список учитывая параметр сортировки (по умолчанию = EVENT_DATE)
        // Сначала события, старт которых ближе всего к текущей дате
        if (sortEventDateOrViews.equals("EVENT_DATE")) {
            eventsForReturn = eventsForReturn.stream()
                    .sorted(Comparator.comparing(Event::getEventDate))
                    .collect(Collectors.toList());

        } else if (sortEventDateOrViews.equals("VIEWS")) {
            eventsForReturn = eventsForReturn.stream()
                    .sorted(Comparator.comparing(Event::getViews))
                    .collect(Collectors.toList());
        }
        // 9. Следующая  итерация, укарачиваем список учитывая параметр from и size (по умолчанию = 0 и 10 соответственно
        // 9.1 Оставляем только мероприятия к просмотру
        eventsForReturn = eventsForReturn.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
        // 9.2 Каждом такому мероприятию увеличиваем счетчик просмотров
        for (Event event : eventsForReturn) {
            event.setViews(event.getViews() + 1L);
            eventRepository.save(event);
        }
        return eventsForReturn.stream()
                .map(eventMapper::eventShortDtoOutputFromEvent)
                .collect(Collectors.toList());

        // 10. Последняя итерация, укарачиваем список учитывая параметр
        // from и size (по умолчанию = 0 и 10 соответственно)
    }

    @Override
    public EventFullDtoOutput getEventByIdPublic(Long eventId) {
        log.debug("Get event by ID using path : '/events/{id}'");
        // 1. Прежде чем вернуть событие, увеличиваем счетчик просмотров на 1
        Event event = eventRepository.getByIdAndStateIs(eventId, StatusOfEvent.PUBLISHED);
        event.setViews(event.getViews() + 1L);
        return eventMapper.eventFullDtoOutputFromEvent(
                eventRepository.save(event)
        );
    }

    @Override
    public List<EventShortDtoOutput> getEventsPrivate(Long userId, Long from, Long size) {
        return null;
    }

    @Override
    public EventFullDtoOutput updateEventPrivate(Long userId, NewEventDTOInput newEventDTOInput) {
        return null;
    }

    @Override
    public EventFullDtoOutput createEventPrivate(Long userId, NewEventDTOInput newEventDTOInput) {
        log.debug("Create event by path : '/users/{userId}/events'");
        Event event = eventMapper.eventFromNewEventDTOInput(newEventDTOInput);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(userRepository.getReferenceById(userId));
        event.setConfirmedRequests(0L);
        event.setState(StatusOfEvent.PENDING);
        event.setViews(0L);
        Event almostFullEvent = eventRepository.save(event);
        return eventMapper.eventFullDtoOutputFromEvent(almostFullEvent);
    }

    @Override
    public EventFullDtoOutput getFullInfoAboutEventByUserWhoCreatedThisEventPrivate(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDtoOutput cancelEventPrivate(Long userId, NewEventDTOInput newEventDTOInput) {
        return null;
    }

    @Override
    public ParticipationRequestDtoOutput getParticipationInformationAboutUserPrivate(Long userId, Long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDtoOutput ApproveParticipationRequestPrivate(Long userId, Long eventId, Long reqId) {
        return null;
    }

    @Override
    public ParticipationRequestDtoOutput refuseParticipationRequestPrivate(Long userId, Long eventId, Long reqId) {
        return null;
    }

    @Override
    public List<EventFullDtoOutput> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                   String rangeStart, String rangeEnd, Long from, Long size) {
        log.debug("Get list with events (ADMIN) by path : 'admin/events'");
        LocalDateTime now = LocalDateTime.now();
        // 1. Все события
        List<Event> eventsForReturn = eventRepository.findAll();
        // 2. Сокращаем список, оставляя только те события, которые соответствуют переданным usersId
        if (users.size() > 0) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(u -> users.contains(u.getInitiator().getId()))
                    .collect(Collectors.toList());
        }
        // 3. Сокращаем список, оставляя только те события, которые соответствуют переданным статусам события
        if (states.size() > 0) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(s -> states.contains(s.getState().toString()))
                    .collect(Collectors.toList());
        }
        // 4. Сокращаем список, оставляя только те события, которые соответствуют переданным categoriesId
        if (states.size() > 0) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(c -> categories.contains(c.getCategory().getId()))
                    .collect(Collectors.toList());
        }
        // 5. Сокращаем список, оставляя только те события, которые соответствуют параметрам дат
        if (rangeStart.length() < 1 && rangeEnd.length() < 1) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(d -> d.getEventDate().isAfter(now))
                    .collect(Collectors.toList());
        }
        if (rangeStart.length() < 1 && rangeEnd.length() > 0) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(d -> d.getEventDate().isAfter(now) &&
                            d.getEventDate().isBefore(converter.ConverterStringToLocalDataTime(rangeEnd)))
                    .collect(Collectors.toList());
        }
        if (rangeStart.length() > 0  && rangeEnd.length() < 1) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(d -> d.getEventDate().isAfter(converter.ConverterStringToLocalDataTime(rangeStart)))
                    .collect(Collectors.toList());
        }
        if (rangeEnd.length() > 0 && rangeStart.length() > 0) {
            eventsForReturn = eventsForReturn.stream()
                    .filter(d -> d.getEventDate().isAfter(converter.ConverterStringToLocalDataTime(rangeStart)) &&
                            d.getEventDate().isBefore(converter.ConverterStringToLocalDataTime(rangeEnd)))
                    .collect(Collectors.toList());
        }
        // 6. Окончательно сокращаем возвращаемый список используя параметры
        // 'from' и 'size' (по умелчанию равны 0 и 10 соотвественно)
        return eventsForReturn.stream()
                .map(eventMapper ::eventFullDtoOutputFromEvent)
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDtoOutput editEventByAdmin(Long eventId, NewEventDTOInput newEventDTOInput) {
        log.debug("Edit event by Admin and eventId follow this path : '/admin/events/{eventId}'");
        Event eventForEdit = eventRepository.getReferenceById(eventId);
        if (newEventDTOInput.getAnnotation() != null && newEventDTOInput.getAnnotation().length() > 0) {
            eventForEdit.setAnnotation(newEventDTOInput.getAnnotation());
        }
        if (newEventDTOInput.getCategoryId() > 0 && newEventDTOInput.getCategoryId() != null) {
            eventForEdit.setCategory(categoryRepository.getReferenceById(newEventDTOInput.getCategoryId()));
        }
        if (newEventDTOInput.getDescription() != null) {
            eventForEdit.setDescription(newEventDTOInput.getDescription());
        }
        if (newEventDTOInput.getEventDate() != null) {
            eventForEdit.setEventDate(newEventDTOInput.getEventDate());
        }
        if (newEventDTOInput.getLocation() != null) {
            eventForEdit.setLat(newEventDTOInput.getLocation().getLat());
            eventForEdit.setLon(newEventDTOInput.getLocation().getLon());
        }
        if (newEventDTOInput.getPaid() != null) {
            eventForEdit.setPaid(newEventDTOInput.getPaid());
        }
        if (newEventDTOInput.getParticipantLimit() > -1) {
            eventForEdit.setParticipantLimit(newEventDTOInput.getParticipantLimit());
        }
        if (newEventDTOInput.getRequestModeration() != null) {
            eventForEdit.setRequestModeration(newEventDTOInput.getRequestModeration());
        }
        if (newEventDTOInput.getTitle() != null && newEventDTOInput.getTitle().length() > 0) {
            eventForEdit.setTitle(newEventDTOInput.getTitle());
        }
        return eventMapper.eventFullDtoOutputFromEvent(eventRepository.save(eventForEdit));
    }

    @Override
    public EventFullDtoOutput publishEventByAdmin(Long eventId) {
        log.debug("Publish event by path : '/admin/events/{eventId}/publish'");
        LocalDateTime nowPlusOneHours = LocalDateTime.now().plusHours(1L);
        if (eventRepository.getByIdAndStateIsAndEventDateIsAfter(eventId, StatusOfEvent.PENDING,
                nowPlusOneHours).getId() != null) {
            Event updatedEvent = eventRepository.getReferenceById(eventId);
            updatedEvent.setPublishedOn(LocalDateTime.now());
            updatedEvent.setState(StatusOfEvent.PUBLISHED);
            return eventMapper.eventFullDtoOutputFromEvent(eventRepository.save(updatedEvent));
        }
        return null;
    }

    @Override
    public EventFullDtoOutput rejectPublishEventByAdmin(Long eventId) {
        log.debug("Reject event by path : '/admin/events/{eventId}/reject'");
        if (eventRepository.getByIdAndStateNot(eventId, StatusOfEvent.PUBLISHED) != null) {
            Event eventReject = eventRepository.getReferenceById(eventId);
            eventReject.setState(StatusOfEvent.CANCELED);
            return eventMapper.eventFullDtoOutputFromEvent(eventRepository.save(eventReject));
        }
        return null;
    }
}
