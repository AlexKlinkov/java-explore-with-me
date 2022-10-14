package ru.practicum.explore_with_me.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDTOInput {
    private Long id;
    @NotEmpty
    @NotNull
    private String name;
}
