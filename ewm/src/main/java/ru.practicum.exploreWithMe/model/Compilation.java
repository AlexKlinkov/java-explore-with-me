package ru.practicum.exploreWithMe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    private Long id;
    private Long eventId;
    private Boolean pinned;
    private String title;
}
