package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "email", length = 256)
    private String email;

    @Column(name = "normalized_email", length = 256)
    private String normalizedEmail;

    @Column(name = "email_confirmed")
    private Boolean emailConfirmed;

    @Column(name = "password_hash", length = 1024)
    private String passwordHash;

    @Column(name = "security_stamp", length = 512)
    private String securityStamp;

    @Column(name = "concurrency_stamp", length = 512)
    private String concurrencyStamp;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(name = "phone_number_confirmed", nullable = false)
    private Boolean phoneNumberConfirmed = false;

    @Column(name = "two_factor_enabled", nullable = false)
    private Boolean twoFactorEnabled = false;

    @Column(name = "lockout_end")
    private Instant lockoutEnd;

    @Column(name = "lockout_enabled", nullable = false)
    private Boolean lockoutEnabled = false;

    @Column(name = "access_failed_count")
    private Integer accessFailedCount;

    @Column(name = "user_name", nullable = false, length = 256)
    private String userName;

    @Column(name = "normalized_user_name", length = 256)
    private String normalizedUserName;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "birthday")
    private LocalDate birthday;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gender_id", nullable = false)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserType userType;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = false;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "external_id", length = 1024)
    private String externalId;

    @Column(name = "unconfirmed_email", length = 256)
    private String unconfirmedEmail;

    @OneToMany(mappedBy = "user")
    private Set<Advertisement> advertisements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Announcement> announcements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Payment> payments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserClaim> userClaims = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserNotification> userNotifications = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Role> roles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserToken> userTokens = new LinkedHashSet<>();

}