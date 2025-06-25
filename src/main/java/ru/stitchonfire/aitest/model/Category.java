package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_id")
    private Category root;

    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "namespace", length = 128)
    private String namespace;

    @Column(name = "seo_info")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> seoInfo;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "modification_date", nullable = false)
    private Instant modificationDate;

    @OneToMany(mappedBy = "category")
    private Set<Announcement> announcements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "root")
    private Set<Category> categories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<CategoryField> categoryFields = new LinkedHashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<Filter> filters = new LinkedHashSet<>();

}