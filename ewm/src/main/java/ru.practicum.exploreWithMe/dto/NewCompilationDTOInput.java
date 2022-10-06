package ru.practicum.exploreWithMe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDTOInput {
    @JsonProperty("events")
    @NotNull
    @NotEmpty
    private List<Long> eventsId; // list with events id for compilation of events
    @Value("false")
    private Boolean pinned; // is a compilation fixed on main page, default value is false
    @NotNull
    @NotEmpty
    private String title; // name of compilation
}
