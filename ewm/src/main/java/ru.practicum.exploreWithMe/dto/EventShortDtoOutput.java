package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.exploreWithMe.auxiliaryObjects.StatusOfEvent;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDtoOutput {
   private Long id; // id of event
   private String annotation; // annotation of event, which describes it (necessary for looking for demanded event)
   private String title; // short description of event
   private CategoryDtoOutput category; // category of event
   private UserShortDtoOutput initiator; // a person who declare about this event
   private Long confirmedRequests; // amount of persons who got permission to visit this event
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private LocalDateTime eventDate; // time of happening of event
   private Boolean paid; // indicator which indicates event is free or for the money
   private Long views; // amount views of persons who was interested in an event
}
