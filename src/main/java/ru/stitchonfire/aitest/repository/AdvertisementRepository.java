package ru.stitchonfire.aitest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stitchonfire.aitest.model.Advertisement;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

    List<Advertisement> findByStatus_Id(Short id);

}
