package ru.practicum.exploreWithMe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.exploreWithMe.auxiliaryObjects.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDTOInput {
    @Size(min = 20, max = 2000)
    private String annotation; // annotation of event, which describes it (necessary for looking for demanded event)
    @Size(min = 3, max = 120)
    private String title; // short description of event
    @Size(min = 20, max = 7000)
    private String description; // full and precise description of event
    @Positive
    @JsonProperty(value = "category")
    private Long categoryId; // id of category
    @Value("0")
    private Long participantLimit; // max amount of persons who can take part in this event,
    // also ZERO means lack of restrictions on filling this event by people
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // time of happening of event
    private Location location; // coordinates where event will be passing
    @Value("false")
    private Boolean paid; // indicator which indicates event is free or for the money
    @Value("true")
    private Boolean requestModeration; // IF true all information about event has to be approved by initiator this
    // event ELSE this confirms information automatically
}
