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
@Table(name = "announcement")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 1023)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private AnnouncementStatus status;

    @Column(name = "price", precision = 14, scale = 2)
    private BigDecimal price;

    @Column(name = "activation_date")
    private Instant activationDate;

    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    @Column(name = "modification_date", nullable = false)
    private Instant modificationDate;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "number_of_views")
    private Integer numberOfViews;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reject_reason", length = 512)
    private String rejectReason;

    @Column(name = "elastic_sync_date")
    private Instant elasticSyncDate;

    @Column(name = "seo_info")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> seoInfo;

    @Column(name = "promotion_end_date")
    private Instant promotionEndDate;

    @Column(name = "promotion_start_date")
    private Instant promotionStartDate;

    @OneToMany(mappedBy = "announcement")
    private Set<AnnouncementField> announcementFields = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "announcement_location",
            joinColumns = @JoinColumn(name = "announcement_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    private Set<Location> locations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "announcement")
    private Set<AnnouncementPayment> announcementPayments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "announcement")
    private Set<AnnouncementRecacheQueue> announcementRecacheQueues = new LinkedHashSet<>();

    @OneToMany(mappedBy = "announcement")
    private Set<AnnouncementResource> announcementResources = new LinkedHashSet<>();

    @OneToMany(mappedBy = "announcement")
    private Set<AvailableSiteUrl> availableSiteUrls = new LinkedHashSet<>();

}