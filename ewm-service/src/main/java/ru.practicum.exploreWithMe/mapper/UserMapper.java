package ru.practicum.exploreWithMe.mapper;

import org.mapstruct.Mapper;
import ru.practicum.exploreWithMe.dto.UserDTOInput;
import ru.practicum.exploreWithMe.dto.UserDtoOutputForAdmin;
import ru.practicum.exploreWithMe.dto.UserShortDtoOutput;
import ru.practicum.exploreWithMe.model.User;

@Mapper
public interface UserMapper {
    UserShortDtoOutput userShortDtoOutputFromUser (User user);
    UserDtoOutputForAdmin userDtoOutputForAdminFromUser(User user);
}
