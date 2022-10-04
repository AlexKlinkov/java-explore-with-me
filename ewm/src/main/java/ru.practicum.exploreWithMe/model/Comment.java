package ru.practicum.exploreWithMe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "authorId")
    private User author;
    @Column(name = "comment")
    private String comment;
    @Column(name = "created")
    private LocalDateTime created;
}
