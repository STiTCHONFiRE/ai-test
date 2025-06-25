package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "announcement_status")
public class AnnouncementStatus {

    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "name")
    private String name;

    @Column(name = "namespace", nullable = false)
    private String namespace;

    @OneToMany(mappedBy = "status")
    private Set<Announcement> announcements = new LinkedHashSet<>();

}