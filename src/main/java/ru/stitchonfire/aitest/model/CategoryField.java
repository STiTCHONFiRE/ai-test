package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "category_field")
public class CategoryField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_type_id")
    private FieldType fieldType;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired = false;

    @Column(name = "regex_validation", length = 511)
    private String regexValidation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_option_list_id")
    private FieldOptionList fieldOptionList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryField parent;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "is_elastic_indexing", nullable = false)
    private Boolean isElasticIndexing = false;

    @OneToMany(mappedBy = "categoryField")
    private Set<AnnouncementField> announcementFields = new LinkedHashSet<>();

    @OneToMany(mappedBy = "parent")
    private Set<CategoryField> categoryFields = new LinkedHashSet<>();

    @OneToMany(mappedBy = "categoryField")
    private Set<Filter> filters = new LinkedHashSet<>();

}