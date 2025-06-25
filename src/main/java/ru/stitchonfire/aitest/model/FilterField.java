package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "filter_field")
public class FilterField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filter_id")
    private Filter filter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_type_id")
    private FieldType fieldType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_option_list_id")
    private FieldOptionList fieldOptionList;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "regex_validation", length = 511)
    private String regexValidation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private FilterField parent;

    @Column(name = "default_value", length = 512)
    private String defaultValue;

    @OneToMany(mappedBy = "parent")
    private Set<FilterField> filterFields = new LinkedHashSet<>();

}