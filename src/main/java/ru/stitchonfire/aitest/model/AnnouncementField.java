package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "announcement_field")
public class AnnouncementField {

    @EmbeddedId
    private AnnouncementFieldId id;

    @MapsId("announcementId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "announcement_id", nullable = false)
    private Announcement announcement;

    @MapsId("categoryFieldId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_field_id", nullable = false)
    private CategoryField categoryField;

    @Column(name = "value")
    private String value;

}