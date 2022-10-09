package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.UserDTOInput;
import ru.practicum.exploreWithMe.dto.UserDtoOutputForAdmin;
import ru.practicum.exploreWithMe.mapper.UserMapper;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.repository.UserRepository;

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
    public List<UserDtoOutputForAdmin> getUsers(List<Long> ids, Long from, Long size) { // GET admin/users
        log.debug("Get users by path '/admin/users' with param 'ids'");
        if (ids != null) {
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
    public UserDtoOutputForAdmin createUser(UserDTOInput userDTOInput) { // POST admin/users
        log.debug("Save user by path '/admin/users'");
        User user = new User();
        user.setName(userDTOInput.getName());
        user.setEmail(userDTOInput.getEmail());
        User userForReturn = userRepository.save(user);
        return userMapper.userDtoOutputForAdminFromUser(userForReturn);
    }

    @Override
    public void deleteUserById(Long userId) { // DELETE admin/users/{userId}
        userRepository.deleteById(userId);
    }
}
