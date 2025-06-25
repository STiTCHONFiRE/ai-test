package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "field_type")
public class FieldType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_list", nullable = false)
    private Boolean isList = false;

    @OneToMany(mappedBy = "fieldType")
    private Set<CategoryField> categoryFields = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fieldType")
    private Set<Filter> filters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fieldType")
    private Set<FilterField> filterFields = new LinkedHashSet<>();

}