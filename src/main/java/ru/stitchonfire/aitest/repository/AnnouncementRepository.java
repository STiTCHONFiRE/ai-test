package ru.stitchonfire.aitest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stitchonfire.aitest.model.Announcement;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

    List<Announcement> findByCategory_IdInAndIsDeletedFalseAndStatus_Id(List<Integer> ids, Short statusId);

}
