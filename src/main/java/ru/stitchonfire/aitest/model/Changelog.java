package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "changelog")
public class Changelog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "type")
    private Short type;

    @Column(name = "version", length = 50)
    private String version;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "name", nullable = false, length = 300)
    private String name;

    @Column(name = "checksum", length = 32)
    private String checksum;

    @Column(name = "installed_by", nullable = false, length = 100)
    private String installedBy;

    @Column(name = "installed_on", nullable = false)
    private Instant installedOn;

    @Column(name = "success", nullable = false)
    private Boolean success = false;

}