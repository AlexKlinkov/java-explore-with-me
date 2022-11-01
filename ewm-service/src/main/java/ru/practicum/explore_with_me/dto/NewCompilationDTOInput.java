package ru.practicum.explore_with_me.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDTOInput {
    @JsonProperty("events")
    @NotNull
    private List<Long> eventsId; // list with events id for compilation of events
    private Boolean pinned; // is a compilation fixed on main page, default value is false
    @NotNull
    private String title; // name of compilation
}
