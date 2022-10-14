package ru.practicum.explore_with_me.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOInput {
    @NotEmpty
    @NotNull
    private String name;
    @Email
    private String email;
}
