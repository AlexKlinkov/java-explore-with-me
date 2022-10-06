package ru.practicum.exploreWithMe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDtoOutputForAdmin {
    @JsonProperty("events")
    private List<Long> eventsId; // list with events id for compilation of events
    private Boolean pinned; // is a compilation fixed on main page, default value is false
    private String title; // name of compilation
}
