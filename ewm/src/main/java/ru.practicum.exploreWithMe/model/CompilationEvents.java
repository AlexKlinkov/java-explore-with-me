package ru.practicum.exploreWithMe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.auxiliaryObjects.client.KeyForCompilationEvents;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(KeyForCompilationEvents.class)
public class CompilationEvents {
    @Id
    private Long compilationId;
    @Id
    private Long eventId;
}
