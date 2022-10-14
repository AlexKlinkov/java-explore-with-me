package ru.practicum.explore_with_me.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDtoOutput {
    private Long id;
    private String name;
}
