package ru.practicum.explore_with_me.service;


import ru.practicum.explore_with_me.dto.UserDTOInput;
import ru.practicum.explore_with_me.dto.UserDtoOutputForAdmin;

import java.util.List;

public interface UserService {
    /** Work with entity 'User' can only admin
        * This method return list of users with definitely ids or if this param is null, then return list with users
        * @param 'from' (indicate amount of first elements which have to be skipped) and
        * @param 'size' (amount of elements in set)
    */
    List<UserDtoOutputForAdmin> getUsers (List<Long> ids, Long from, Long size); // GET admin/users
    UserDtoOutputForAdmin createUser (UserDTOInput userDTOInput); // POST admin/users
    void deleteUserById (Long userId); // DELETE admin/users/{userId}
}
