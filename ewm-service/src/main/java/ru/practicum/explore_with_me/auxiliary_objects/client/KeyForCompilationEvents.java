package ru.practicum.explore_with_me.auxiliary_objects.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyForCompilationEvents implements Serializable {
    private Long compilationId;
    private Long eventId;
}
