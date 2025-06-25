package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "elastic_sync_type")
public class ElasticSyncType {
    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "name", length = 254)
    private String name;

    @OneToMany(mappedBy = "type")
    private Set<ElasticSyncHistory> elasticSyncHistories = new LinkedHashSet<>();

}