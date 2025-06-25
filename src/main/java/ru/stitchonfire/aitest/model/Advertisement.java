package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "advertisement")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private AdvertisementStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "link_url", length = 2048)
    private String linkUrl;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "title", nullable = false, length = 256)
    private String title;

    @Column(name = "reject_reason", length = 512)
    private String rejectReason;

    @Column(name = "has_desktop_banner", nullable = false)
    private Boolean hasDesktopBanner = false;

    @Column(name = "has_mobile_banner", nullable = false)
    private Boolean hasMobileBanner = false;

    @Column(name = "modification_date")
    private Instant modificationDate;

    @Column(name = "user_phone_number", nullable = false, length = 50)
    private String userPhoneNumber;

    @OneToMany(mappedBy = "advertisement")
    private Set<AdvertisementDate> advertisementDates = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "advertisement_location",
            joinColumns = @JoinColumn(name = "advertisement_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    private Set<Location> locations = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "advertisement_payment",
            joinColumns = @JoinColumn(name = "advertisement_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_id"))
    private Set<Payment> payments = new LinkedHashSet<>();

}