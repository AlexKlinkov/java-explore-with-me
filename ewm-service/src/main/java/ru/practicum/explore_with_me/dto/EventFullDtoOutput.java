package ru.practicum.explore_with_me.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explore_with_me.auxiliary_objects.Location;
import ru.practicum.explore_with_me.auxiliary_objects.StatusOfEvent;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDtoOutput {
    private Long id; // id of event
    private String annotation; // annotation of event, which short describes it
    private String title; // short description of event
    private String description; // full and precise description of event
    private CategoryDtoOutput category; // category of event
    private UserShortDtoOutput initiator; // a person who declare about this event
    private Long confirmedRequests; // amount of persons who got permission to visit this event
    private Long participantLimit; // max amount of persons who can take part in this event
    private Location location; // coordinates where event will be passing
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn; // date when initiator declared about event
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn; // date when moderator checked this event with positive status
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // time of happening of event
    private StatusOfEvent state; // status of event {PUBLISHED, WAITING....}
    private Boolean paid; // indicator which indicates event is free or for the money
    private Long views; // amount views of persons who was interested in an event
    private Boolean requestModeration; // initiator has to confirm claim of requestor for visiting this event,
    // default is true
}
