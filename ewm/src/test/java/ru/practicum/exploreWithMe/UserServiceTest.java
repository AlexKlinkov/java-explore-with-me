package ru.practicum.exploreWithMe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.exploreWithMe.dto.UserDTOInput;
import ru.practicum.exploreWithMe.dto.UserDtoOutputForAdmin;
import ru.practicum.exploreWithMe.mapper.UserMapperImplForBD;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.repository.UserRepository;
import ru.practicum.exploreWithMe.service.UserService;
import ru.practicum.exploreWithMe.service.UserServiceInBD;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceInBD userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapperImplForBD userMapper;
    UserDTOInput userDTOInput;
    UserDtoOutputForAdmin userDtoOutputForAdmin;
    User user;

    @BeforeEach
    public void init() {
        userDTOInput = new UserDTOInput("Max", "max@created.ru");
        userDtoOutputForAdmin = new UserDtoOutputForAdmin(
                1L, "Max", "max@created.ru"
        );
        user = new User(1L, "Max", "max@created.ru");
    }

    @Test
    public void getUsersTestWhenIdsIs1() {
        List<User> users = new ArrayList<>();
        users.add(user);
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        when(userRepository.getAll()).thenReturn(users);
        userService.getUsers(ids, null, null);
        Assertions.assertEquals(user.getId(), userRepository.getAll().get(0).getId());
    }
    @Test
    public void getUsersTestFrom0Size1() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.getAll()).thenReturn(users);
        userService.getUsers(null, 0L, 1L);
        Assertions.assertEquals(user.getId(), userRepository.getAll().get(0).getId());
    }

    @Test
    public void createUserTest() {
        when(userRepository.save(user)).thenReturn(user);
        userService.createUser(userDTOInput);
        Assertions.assertEquals(user.getId(), userRepository.save(user).getId());
    }

    @Test
    public void deleteUserTest() {
        // UserRepository is just a mock
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        // Creating the real UserServiceInBD
        UserServiceInBD userServiceInBDReal = new UserServiceInBD(userRepositoryMock);
        // Creating a spy proxy
        UserServiceInBD spy = spy(userServiceInBDReal);
        spy.deleteUserById(user.getId());
        verify(userRepositoryMock, times(1)).deleteById(user.getId());
    }
}
