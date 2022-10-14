package ru.practicum.explore_with_me.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore_with_me.auxiliary_objects.StatusOfEvent;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id of event
    @Column(name = "annotation")
    private String annotation; // annotation of event, which describes it (necessary for looking for demanded event)
    @Column(name = "title")
    private String title; // short description of event
    @Column(name = "description")
    private String description; // full and precise description of event
    @ManyToOne
    @JoinColumn(name = "category_id") // this reference on table with category information
    private Category category;
    @ManyToOne
    @JoinColumn(name = "initiator_id") // this reference on table with users information
    private User initiator;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests; // amount of persons who confirmed entrance
    @Column(name = "participant_limit")
    private Long participantLimit; // max amount of persons who can take part in this event
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lon")
    private Double lon;
    @Column(name = "created_on")
    private LocalDateTime createdOn; // date when initiator declared about event
    @Column(name = "published_on")
    private LocalDateTime publishedOn; // date when moderator checked this event with positive status
    @Column(name = "event_date")
    private LocalDateTime eventDate; // time of happening of event
    @Column(name = "state", columnDefinition = "enum('PUBLISHED','PENDING','CANCELED')")
    @Enumerated(EnumType.STRING)
    private StatusOfEvent state; // status of event {PUBLISHED, WAITING....}
    @Column(name = "paid")
    private Boolean paid; // indicator which indicates event is free or for the money
    @Column(name = "views")
    private Long views; // amount views of persons who was interested in an event
    @Column(name = "request_moderation")
    private Boolean requestModeration; // flag is indicated to fix something from Admin
}
