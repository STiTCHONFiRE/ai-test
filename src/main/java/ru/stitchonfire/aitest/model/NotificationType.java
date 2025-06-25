package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "notification_type")
public class NotificationType {
    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "name", nullable = false, length = 512)
    private String name;

    @Column(name = "template", nullable = false, length = Integer.MAX_VALUE)
    private String template;

    @Column(name = "subject", length = 500)
    private String subject;

    @OneToMany(mappedBy = "type")
    private Set<UserNotification> userNotifications = new LinkedHashSet<>();

}