package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.model.Category;

import javax.transaction.Transactional;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT c FROM Category c")
    List<Category> getAll (); // return list with all users who exist
    @Transactional
    @Modifying
    @Query("update Category c set c.name = :name where c.id = :id")
    Category update (@Param("id") Long id, @Param("name") String name);
}
