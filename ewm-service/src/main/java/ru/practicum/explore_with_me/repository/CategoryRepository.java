package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.model.Category;

import javax.transaction.Transactional;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT c FROM Category c")
    Page<Category> findAllWithPagination(Pageable pageable);
    @Transactional
    @Modifying
    @Query("update Category c set c.name = :name where c.id = :id")
    void update (@Param("id") Long id, @Param("name") String name);
}
