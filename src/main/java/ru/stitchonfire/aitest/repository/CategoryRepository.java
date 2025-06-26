package ru.stitchonfire.aitest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stitchonfire.aitest.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAllByIdIn(List<Integer> ids);

}
