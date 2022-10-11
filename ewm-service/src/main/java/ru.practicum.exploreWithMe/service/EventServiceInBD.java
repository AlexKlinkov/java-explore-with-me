package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.auxiliaryObjects.Converter;
import ru.practicum.exploreWithMe.auxiliaryObjects.StatusOfEvent;
import ru.practicum.exploreWithMe.auxiliaryObjects.StatusOfParticipationRequest;
import ru.practicum.exploreWithMe.dto.EventFullDtoOutput;
import ru.practicum.exploreWithMe.dto.EventShortDtoOutput;
import ru.practicum.exploreWithMe.dto.NewEventDTOInput;
import ru.practicum.exploreWithMe.dto.ParticipationRequestDtoOutput;
import ru.practicum.exploreWithMe.exeption.*;
import ru.practicum.exploreWithMe.mapper.EventMapper;
import ru.practicum.exploreWithMe.mapper.ParticipationRequestMapper;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.ParticipationRequest;
import ru.practicum.exploreWithMe.repository.CategoryRepository;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.ParticipationRequestRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
    private ParticipationRequestRepository participationRequestRepository;
    @Autowired
    @Qualifier("EventMapperImplForBD")
    final EventMapper eventMapper;
    @Autowired
    @Qualifier("ParticipationRequestMapperImplForBD")
    final ParticipationRequestMapper participationRequestMapper;
    @Autowired
    private Converter converter;

    @Override
    public List<EventShortDtoOutput> getEventsPublic(String text, List<Long> categoryIds, Boolean paid,
                                                     String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                     String sortEventDateOrViews, Long from, Long size) {
        log.debug("Get list with Published events (PUBLIC) by path : '/events'");
        if (size < 1 || from < 0) {
            throw new NotCorrectArgumentsInMethodException("Size cannot be less than 1, also " +
                    "from cannot be less then 0");
        }
        if (!sortEventDateOrViews.equals("EVENT_DATE") && !sortEventDateOrViews.equals("VIEWS")) {
            throw new NotCorrectArgumentsInMethodException("Put incorrect value of sort, is " + sortEventDateOrViews +
                    " but there can be only one of them : 1.EVENT_DATE or 2.VIEWS");
        }
        if (converter.ConverterStringToLocalDataTime(rangeStart).isAfter(
                converter.ConverterStringToLocalDataTime(rangeEnd))) {
            throw new NotCorrectArgumentsInMethodException("Data start chosen is " + rangeStart + " cannot be " +
                    "after chosen data end is " + rangeEnd);
        }
        try {
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
                List<Event> onlyByWithIds = eventsForReturn.stream()
                        .filter(i -> categoryIds.contains(i.getId()))
                        .collect(Collectors.toList());
                if (!onlyByWithIds.isEmpty()) {
                    eventsForReturn = onlyByWithIds;
                }
            }
            // 5. Следущая итерация, укарачиваем список с помощью параметра paid (по умолчанию = false)
            eventsForReturn = eventsForReturn.stream()
                    .filter(p -> p.getPaid().equals(paid))
                    .collect(Collectors.toList());
            // 6. Следущая итерация, укарачиваем список с помощью параметра диапазона дат
            if (rangeStart.length() > 0 && rangeEnd.length() > 0) {
                eventsForReturn = eventsForReturn.stream()
                        .filter(d -> d.getEventDate().isAfter(converter.ConverterStringToLocalDataTime(rangeStart)) &&
                                d.getEventDate().isBefore(converter.ConverterStringToLocalDataTime(rangeEnd)))
                        .collect(Collectors.toList());
            }
            if (rangeStart.length() > 0 && rangeEnd.length() < 1) {
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
            // 9. Следующая  итерация, укарачиваем список учитывая параметр from и size
            // (по умолчанию = 0 и 10 соответственно
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
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public EventFullDtoOutput getEventByIdPublic(Long eventId) {
        log.debug("Get event by ID using path : '/events/{id}'");
        if (eventId < 0) {
            throw new ValidationException("Id of event cannot be less than 0");
        } else if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        try {
            // 1. Прежде чем вернуть событие, увеличиваем счетчик просмотров на 1
            Event event = eventRepository.getByIdAndStateIs(eventId, StatusOfEvent.PUBLISHED);
            event.setViews(event.getViews() + 1L);
            return eventMapper.eventFullDtoOutputFromEvent(
                    eventRepository.save(event)
            );
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public List<EventFullDtoOutput> getEventsPrivate(Long userId, Long from, Long size) {
        log.debug("Get all events which belong initiator of these events by path : '/users/{userId}/events'");
        if (size < 1 || from < 0) {
            throw new NotCorrectArgumentsInMethodException("Size cannot be less than 1, " +
                    "also from cannot be less then 0");
        }
        try {
            return eventRepository.getAllByInitiatorId(userId).stream()
                    .map(eventMapper::eventFullDtoOutputFromEvent)
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public EventFullDtoOutput updateEventPrivate(Long userId, NewEventDTOInput newEventDTOInput) {
        log.debug("PATCH initiator update own event by path : '/users/{userId}/events'");
        if (userId < 0) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        try {
            LocalDateTime now = LocalDateTime.now();
            Event event = eventRepository.getByInitiatorId(userId);
            if (event.getState().equals(StatusOfEvent.CANCELED) || event.getState().equals(StatusOfEvent.PENDING)) {
                event.setState(StatusOfEvent.PENDING);
                event.setAnnotation(newEventDTOInput.getAnnotation());
                if (categoryRepository.existsById(newEventDTOInput.getCategoryId())) {
                    event.setCategory(categoryRepository.getReferenceById(newEventDTOInput.getCategoryId()));
                }
                event.setDescription(newEventDTOInput.getDescription());
                if (newEventDTOInput.getEventDate().isAfter(now.plusHours(2L))) {
                    event.setEventDate(newEventDTOInput.getEventDate());
                }
                event.setPaid(newEventDTOInput.getPaid());
                event.setParticipantLimit(newEventDTOInput.getParticipantLimit());
                event.setTitle(newEventDTOInput.getTitle());
                return eventMapper.eventFullDtoOutputFromEvent(eventRepository.save(event));
            } else {
                throw new ConflictException("Event with id=" + event.getId() + " cannot be updated, " +
                        "coz status is not 'CANCELED' or 'PENDING', your status is " + event.getState());
            }
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public EventFullDtoOutput createEventPrivate(Long userId, NewEventDTOInput newEventDTOInput) {
        log.debug("Create event by path : '/users/{userId}/events'");
        if (userId < 0) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        try {
            LocalDateTime now = LocalDateTime.now();
            if (newEventDTOInput.getEventDate().isAfter(now.plusHours(2L))) {
                Event event = eventMapper.eventFromNewEventDTOInput(newEventDTOInput);
                event.setCreatedOn(now);
                event.setInitiator(userRepository.getReferenceById(userId));
                event.setConfirmedRequests(0L);
                event.setState(StatusOfEvent.PENDING);
                event.setViews(0L);
                Event almostFullEvent = eventRepository.save(event);
                return eventMapper.eventFullDtoOutputFromEvent(almostFullEvent);
            } else {
                throw new ConflictException("New event with data start=" + newEventDTOInput.getEventDate() +
                        " cannot be created, coz data start has to be minimum for 2 hours from now");
            }
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public EventFullDtoOutput getFullInfoAboutEventByUserWhoCreatedThisEventPrivate(Long userId, Long eventId) {
        log.debug("Get all information about event by initiator follow path : '/users/{userId}/events/{eventId}'");
        if (eventId < 0) {
            throw new ValidationException("Id of event cannot be less than 0");
        } else if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        if (userId < 0) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        if (eventRepository.getReferenceById(eventId).getInitiator().getId() != userId) {
            throw new ConflictException("Event with id=" + eventId + " cannot be got, coz user with id="
                    + userId + " didn't create it");
        }
        try {
            return eventMapper.eventFullDtoOutputFromEvent(eventRepository.getByIdAndInitiatorId(eventId, userId));
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public EventFullDtoOutput cancelEventPrivate(Long userId, Long eventId) {
        log.debug("Cancel event by initiator follow path : '/users/{userId}/events/{eventId}'");
        if (userId < 0) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (eventId < 0) {
            throw new ValidationException("Id of event cannot be less than 0");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        if (!Objects.equals(eventRepository.getReferenceById(eventId).getInitiator().getId(), userId)) {
            throw new ConflictException("Event with id=" + eventId + " cannot be canceled, coz user with id="
                    + userId + " didn't create it");
        }
        try {
            Event event = eventRepository.getByIdAndInitiatorId(eventId, userId);
            if (event.getState().equals(StatusOfEvent.PENDING)) {
                event.setState(StatusOfEvent.CANCELED);
                return eventMapper.eventFullDtoOutputFromEvent(eventRepository.save(event));
            } else {
                throw new ConflictException("Event with id=" + eventId + " cannot be canceled " +
                        "coz was already approved");
            }
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public List<ParticipationRequestDtoOutput> getParticipationInformationAboutUserPrivate(Long userId, Long eventId) {
        log.debug("Initiator of event obtains information about participation request at this event by path :" +
                " '/users/{userId}/events/{eventId}/requests'");
        if (eventId < 0) {
            throw new ValidationException("Id of event cannot be less than 0");
        }
        if (userId < 0) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        try {
            return participationRequestRepository.findAll().stream()
                    .filter(i -> i.getRequestor().getId() != userId && i.getEvent().getId() == eventId)
                    .map(participationRequestMapper::RequestDtoOutputFromParticipationRequest)
                    .collect(Collectors.toList());
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public ParticipationRequestDtoOutput ApproveParticipationRequestPrivate(Long userId, Long eventId, Long reqId) {
        log.debug("Initiator of event approves participation request at this event by path :" +
                " '/users/{userId}/events/{eventId}/requests/{reqId}/confirm'");
        if (eventId < 0) {
            throw new ValidationException("Id of event cannot be less than 0");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        if (reqId < 0) {
            throw new ValidationException("Id of participationRequest cannot be less than 0");
        }
        if (!participationRequestRepository.existsById(reqId)) {
            throw new NotFoundException("ParticipationRequest with id=" + reqId + " was not found.");
        }
        if (userId < 0) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        try {
            // 1. Получаем сначала событие текущего пользователя (инициатора события)
            Event event = eventRepository.getByIdAndInitiatorId(eventId, userId);
            // 2. Получаем заявку на участие в мероприятии
            ParticipationRequest participationRequest = participationRequestRepository.getReferenceById(reqId);
            if (event.getParticipantLimit() != 0 || event.getRequestModeration().equals(true)) {
                // 3. Это событие требуют подтверждение, если не достиг предел на учатие по количеству человек
                // в мероприятии
                if (event.getParticipantLimit() != event.getConfirmedRequests()) {
                    // 4. Обновляем статус запроса
                    participationRequest.setStatus(StatusOfParticipationRequest.CONFIRMED);
                    participationRequestRepository.save(participationRequest);
                    // 5. У события увеличиваем счетчик подтвержденных заявок
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
                    eventRepository.save(event);
                }
                // 6. Проверяем достиг ли лимит по количеству участников в мероприятии, если да, то оставшиеся
                // заявки на участие отклоняем
                if (event.getParticipantLimit() == event.getConfirmedRequests()) {
                    // 6.1 Получаем все заявки на события, которые имеют статус PENDING
                    List<ParticipationRequest> participationRequestList =
                            participationRequestRepository.getAllByEventIdAndStatusIs(
                                    eventId, StatusOfParticipationRequest.PENDING);
                    // 6.2 Меняем статус всех этих заявок на CANCELED
                    for (ParticipationRequest request : participationRequestList) {
                        request.setStatus(StatusOfParticipationRequest.REJECTED);
                        participationRequestRepository.save(request);
                    }
                }
            }
            return participationRequestMapper.RequestDtoOutputFromParticipationRequest(
                    participationRequestRepository.getReferenceById(reqId));
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public ParticipationRequestDtoOutput refuseParticipationRequestPrivate(Long userId, Long eventId, Long reqId) {
        log.debug("Initiator of event refuse participation request at this event by path :" +
                " '/users/{userId}/events/{eventId}/requests/{reqId}/reject'");
        if (eventId < 0) {
            throw new ValidationException("Id of event cannot be less than 0");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        if (userId < 0) {
            throw new ValidationException("Id of user cannot be less than 0");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        if (reqId < 0) {
            throw new ValidationException("Id of participationRequest cannot be less than 0");
        }
        if (!participationRequestRepository.existsById(reqId)) {
            throw new NotFoundException("ParticipationRequest with id=" + reqId + " was not found.");
        }
        try {
            // 1. Получаем сначала событие текущего пользователя (инициатора события)
            Event event = eventRepository.getByIdAndInitiatorId(eventId, userId);
            // 2. Получаем заявку на участие в мероприятии
            ParticipationRequest participationRequest = participationRequestRepository.getReferenceById(reqId);
            // 3. Проверяем предварительно статус заявки
            if (participationRequest.getStatus().equals(StatusOfParticipationRequest.CONFIRMED)) {
                event.setConfirmedRequests(event.getConfirmedRequests() - 1L);
                eventRepository.save(event);
            }
            // 4. Отклоняем заявку на участие в мероприятии
            participationRequest.setStatus(StatusOfParticipationRequest.REJECTED);
            participationRequestRepository.save(participationRequest);
            return participationRequestMapper.RequestDtoOutputFromParticipationRequest(
                    participationRequestRepository.getReferenceById(reqId));
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public List<EventFullDtoOutput> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                   String rangeStart, String rangeEnd, Long from, Long size) {
        log.debug("Get list with events (ADMIN) by path : 'admin/events'");
        if (size < 1 || from < 0) {
            throw new NotCorrectArgumentsInMethodException("Size cannot be less than 1, " +
                    "also from cannot be less then 0");
        }
        if (converter.ConverterStringToLocalDataTime(rangeStart).isAfter(
                converter.ConverterStringToLocalDataTime(rangeEnd))) {
            throw new NotCorrectArgumentsInMethodException("Data start chosen is " + rangeStart + " cannot be " +
                    "after chosen data end is " + rangeEnd);
        }
        try {
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
            if (rangeStart.length() > 0 && rangeEnd.length() < 1) {
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
                    .map(eventMapper::eventFullDtoOutputFromEvent)
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public EventFullDtoOutput editEventByAdmin(Long eventId, NewEventDTOInput newEventDTOInput) {
        log.debug("Edit event by Admin and eventId follow this path : '/admin/events/{eventId}'");
        if (eventId < 0) {
            throw new ValidationException("Id of event cannot be less than 0");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        try {
            Event eventForEdit = eventRepository.getReferenceById(eventId);
            eventForEdit.setAnnotation(newEventDTOInput.getAnnotation());
            if (categoryRepository.existsById(newEventDTOInput.getCategoryId())) {
                eventForEdit.setCategory(categoryRepository.getReferenceById(newEventDTOInput.getCategoryId()));
            }
            eventForEdit.setDescription(newEventDTOInput.getDescription());
            eventForEdit.setEventDate(newEventDTOInput.getEventDate());
            if (newEventDTOInput.getLocation() != null) {
                eventForEdit.setLat(newEventDTOInput.getLocation().getLat());
                eventForEdit.setLon(newEventDTOInput.getLocation().getLon());
            }
            eventForEdit.setPaid(newEventDTOInput.getPaid());
            eventForEdit.setParticipantLimit(newEventDTOInput.getParticipantLimit());
            eventForEdit.setRequestModeration(newEventDTOInput.getRequestModeration());
            eventForEdit.setTitle(newEventDTOInput.getTitle());
            return eventMapper.eventFullDtoOutputFromEvent(eventRepository.save(eventForEdit));
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public EventFullDtoOutput publishEventByAdmin(Long eventId) {
        log.debug("Publish event by path : '/admin/events/{eventId}/publish'");
        if (eventId < 0) {
            throw new ValidationException("Id of event cannot be less than 0");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nowPlusOneHours = now.plusHours(1L);
            if (eventRepository.getByIdAndStateIsAndEventDateIsAfter(eventId, StatusOfEvent.PENDING,
                    nowPlusOneHours).getId() != null) {
                Event updatedEvent = eventRepository.getReferenceById(eventId);
                updatedEvent.setPublishedOn(now);
                updatedEvent.setState(StatusOfEvent.PUBLISHED);
                Event eventForReturn = eventRepository.save(updatedEvent);
                return eventMapper.eventFullDtoOutputFromEvent(eventForReturn);
            } else {
                throw new ConflictException("Event with id=" + eventId + " cannot be published because of :" +
                        " 1. might status is not PENDING, your status is " +
                        eventRepository.getReferenceById(eventId).getState() +
                        " 2. might published date is less than for 1 hours to event's start, different is " +
                        Duration.between(eventRepository.getReferenceById(eventId).getEventDate(), now).toMinutes()
                        + " min");
            }
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }

    @Override
    public EventFullDtoOutput rejectPublishEventByAdmin(Long eventId) {
        log.debug("Reject event by path : '/admin/events/{eventId}/reject'");
        if (eventId < 0) {
            throw new ValidationException("Id of event cannot be less than 0");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found.");
        }
        try {
            if (eventRepository.getByIdAndStateNot(eventId, StatusOfEvent.PUBLISHED) != null) {
                Event eventReject = eventRepository.getReferenceById(eventId);
                eventReject.setState(StatusOfEvent.CANCELED);
                return eventMapper.eventFullDtoOutputFromEvent(eventRepository.save(eventReject));
            } else {
                throw new ConflictException("Event with id=" + eventId + " cannot be rejected so before this " +
                        "event wan not published");
            }
        } catch (ServerError exception) {
            throw new ServerError("Error occurred");
        }
    }
}
