package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "field_option_list_value")
public class FieldOptionListValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_option_list_id")
    private FieldOptionList fieldOptionList;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private FieldOptionListValue parent;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @OneToMany(mappedBy = "parent")
    private Set<FieldOptionListValue> fieldOptionListValues = new LinkedHashSet<>();

}