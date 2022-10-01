package ru.practicum.exploreWithMe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.exploreWithMe.dto.UserDTOInput;
import ru.practicum.exploreWithMe.dto.UserDtoOutputForAdmin;
import ru.practicum.exploreWithMe.mapper.UserMapperImplForBD;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.repository.UserRepository;
import ru.practicum.exploreWithMe.service.UserServiceInBD;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
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
    public void init () {
        userDTOInput = new UserDTOInput("Max", "max@created.ru");
        userDtoOutputForAdmin = new UserDtoOutputForAdmin(
                1L,"Max", "max@created.ru"
        );
        user = new User(1L,"Max", "max@created.ru");
    }

    @Test
    public void createUserTest() {
        when(userMapper.userFromDTOInputUser(userDTOInput)).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.userDtoOutputForAdminFromUser(user)).thenReturn(userDtoOutputForAdmin);
        Assertions.assertEquals(user.getName(), userMapper.userFromDTOInputUser(userDTOInput).getName());
        //userService.createUser(userDTOInput);
        Assertions.assertEquals(user.getId(), userRepository.save(user).getId());
        Assertions.assertEquals(user.getName(), userMapper.userDtoOutputForAdminFromUser(user).getName());
    }

/*    @Test
    public void deleteUserTest() {
       doNothing().when(userRepository).deleteById(anyLong());
        userService.deleteUserById(1L);
        verify(userRepository, Mockito.times(1)).deleteById(anyLong());
    }*/
}
