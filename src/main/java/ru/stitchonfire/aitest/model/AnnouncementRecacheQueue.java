package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "announcement_recache_queue")
public class AnnouncementRecacheQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "announcement_id", nullable = false)
    private Announcement announcement;

    @Column(name = "is_removed", nullable = false)
    private Boolean isRemoved = false;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;

}