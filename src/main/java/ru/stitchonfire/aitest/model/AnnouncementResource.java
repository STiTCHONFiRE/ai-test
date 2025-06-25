package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "announcement_resource")
public class AnnouncementResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @Column(name = "type", nullable = false, length = 63)
    private String type;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "metadata", length = 1023)
    private String metadata;

    @Column(name = "url", nullable = false, length = 1087)
    private String url;

}