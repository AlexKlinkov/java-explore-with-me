package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.auxiliary_objects.CompilationCheckValidationMethods;
import ru.practicum.explore_with_me.dto.UserDTOInput;
import ru.practicum.explore_with_me.dto.UserDtoOutputForAdmin;
import ru.practicum.explore_with_me.exeption.*;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Component("UserServiceInBD")
@RequiredArgsConstructor
public class UserServiceInBD implements UserService {
    // Work with entity 'User' can only admin

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    @Qualifier("UserMapperImplForBD")
    private UserMapper userMapper;

    @Override
    public List<UserDtoOutputForAdmin> getUsers(List<Long> ids, Long from, Long size) {
        log.debug("Get users by path '/admin/users' with param 'ids'");
        CompilationCheckValidationMethods.checkParamsOfPageFromAndSize(from, size);
        if (ids != null && !ids.isEmpty()) {
            return userRepository.getAllByIdIn(ids).stream()
                    .map((user) -> userMapper.userDtoOutputForAdminFromUser(user))
                    .collect(Collectors.toList());
        }
        log.debug("Get users by path '/admin/users' with params 'from' (default is 0) and 'size' (default is 10)");
        return userRepository.getAll().stream()
                .map((user) -> userMapper.userDtoOutputForAdminFromUser(user))
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public UserDtoOutputForAdmin createUser(UserDTOInput userDTOInput) {
        log.debug("Save user by path '/admin/users'");
        try {
            User user = new User();
            user.setName(userDTOInput.getName());
            user.setEmail(userDTOInput.getEmail());
            return userMapper.userDtoOutputForAdminFromUser(userRepository.save(user));
        } catch (ConflictException exception) {
            throw new ConflictException("User with this email " + userDTOInput.getEmail() + " already exist");
        }
    }

    @Override
    public void deleteUserById(Long userId) {
        CompilationCheckValidationMethods.checkParamOfId(userId, "UserId");
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found.");
        }
        userRepository.deleteById(userId);
    }
}
