package ru.practicum.exploreWithMe.mapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.UserDtoOutputForAdmin;
import ru.practicum.exploreWithMe.dto.UserShortDtoOutput;
import ru.practicum.exploreWithMe.model.User;

@Data
@Slf4j
@Component("UserMapperImplForBD")
public class UserMapperImplForBD implements UserMapper {
    @Override
    public UserShortDtoOutput userShortDtoOutputFromUser(User user) {
        if ( user == null ) {
            return null;
        }

        UserShortDtoOutput userShortDtoOutput = new UserShortDtoOutput();
        userShortDtoOutput.setId(user.getId());
        userShortDtoOutput.setName(user.getName());

        return userShortDtoOutput;
    }

    @Override
    public UserDtoOutputForAdmin userDtoOutputForAdminFromUser(User user) {
        if ( user == null ) {
            return null;
        }

        UserDtoOutputForAdmin userDtoOutputForAdmin = new UserDtoOutputForAdmin();
        userDtoOutputForAdmin .setId(user.getId());
        userDtoOutputForAdmin .setName(user.getName());
        userDtoOutputForAdmin.setEmail(user.getEmail());

        return userDtoOutputForAdmin ;
    }
}
