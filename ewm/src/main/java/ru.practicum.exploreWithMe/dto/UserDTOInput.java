package ru.practicum.exploreWithMe.dto;

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
    private Long id;
    @NotEmpty
    @NotNull
    private String name;
    @Email
    @NotNull
    @NotEmpty
    private String email;
}
