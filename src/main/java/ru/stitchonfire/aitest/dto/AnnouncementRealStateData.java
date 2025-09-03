package ru.stitchonfire.aitest.dto;

import ru.stitchonfire.aitest.model.Announcement;

import java.util.Map;

public record AnnouncementRealStateData(
        String name,
        Map<String, Announcement> announcementMap
) {
}
