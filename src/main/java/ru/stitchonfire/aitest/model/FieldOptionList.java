package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "field_option_list")
public class FieldOptionList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "fieldOptionList")
    private Set<CategoryField> categoryFields = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fieldOptionList")
    private Set<FieldOptionListValue> fieldOptionListValues = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fieldOptionList")
    private Set<FilterField> filterFields = new LinkedHashSet<>();

}