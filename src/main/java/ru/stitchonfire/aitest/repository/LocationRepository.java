package ru.stitchonfire.aitest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.stitchonfire.aitest.model.Location;

import java.time.Instant;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("select distinct l from Location l " +
            "left join fetch l.announcements a " +
            "where a.category.id = :categoryId " +
            "and a.isDeleted = false " +
            "and a.status.id = 2 " +
            "and a.activationDate >= :filterDate")
    List<Location> getLocationsWithFilteredDate(Integer categoryId, Instant filterDate);

    @Query("select distinct l from Location l " +
            "left join fetch l.announcements a " +
            "where a.category.id = :categoryId " +
            "and a.isDeleted = false " +
            "and a.status.id = 2")
    List<Location> getLocations(Integer categoryId);

}
