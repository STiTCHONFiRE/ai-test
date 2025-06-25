package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role_claims")
public class RoleClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "claim_type", nullable = false, length = 256)
    private String claimType;

    @Column(name = "claim_value", nullable = false, length = 256)
    private String claimValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleid")
    private Role roleid;

}