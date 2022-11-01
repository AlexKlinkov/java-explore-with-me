package ru.practicum.explore_with_me.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore_with_me.dto.UserDtoOutputForAdmin;
import ru.practicum.explore_with_me.dto.UserShortDtoOutput;
import ru.practicum.explore_with_me.model.User;

@Mapper
public interface UserMapper {
    UserShortDtoOutput userShortDtoOutputFromUser (User user);
    UserDtoOutputForAdmin userDtoOutputForAdminFromUser(User user);
}
