package ru.stitchonfire.aitest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stitchonfire.aitest.model.FieldOptionListValue;

public interface FieldOptionListValueRepository extends JpaRepository<FieldOptionListValue, Integer> {
}
