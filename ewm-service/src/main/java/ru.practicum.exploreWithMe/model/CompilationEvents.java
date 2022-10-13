package ru.practicum.exploreWithMe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.auxiliary_objects.client.KeyForCompilationEvents;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(KeyForCompilationEvents.class)
public class CompilationEvents {
    @Id
    @JoinColumn(name = "compilation_id")
    private Long compilationId;
    @Id
    @JoinColumn(name = "event_id")
    private Long eventId;
}
