package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Location parent;

    @Column(name = "max_active_advertisement_count")
    private Integer maxActiveAdvertisementCount;

    @Column(name = "advertisement_day_price", precision = 7, scale = 2)
    private BigDecimal advertisementDayPrice;

    @Column(name = "seo_info")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> seoInfo;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "modification_date", nullable = false)
    private Instant modificationDate;

    @ManyToMany(mappedBy = "locations")
    private Set<Advertisement> advertisements = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "locations")
    private Set<Announcement> announcements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "parent")
    private Set<Location> locations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "location")
    private Set<User> users = new LinkedHashSet<>();

}