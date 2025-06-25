package ru.stitchonfire.aitest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stitchonfire.aitest.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {



}
