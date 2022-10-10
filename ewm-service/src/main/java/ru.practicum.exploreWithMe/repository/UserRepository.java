package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.model.User;

import java.util.List;
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u FROM User u WHERE u.id IN :id")
    List<User> getAllByIdIn (@Param("id")List<Long> ids); // return list with users which have diffidently ids
    @Query(value = "SELECT u FROM User u")
    List<User> getAll (); // return list with all users who exist
}
