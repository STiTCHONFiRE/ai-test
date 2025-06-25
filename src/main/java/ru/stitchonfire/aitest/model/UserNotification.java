package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_notification")
public class UserNotification {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private NotificationType type;

    @Column(name = "recipient", nullable = false, length = 128)
    private String recipient;

    @Column(name = "status", nullable = false, length = 128)
    private String status;

    @Column(name = "external_message_id", length = 256)
    private String externalMessageId;

    @Column(name = "smtp_id", length = 256)
    private String smtpId;

    @Column(name = "error_reason", length = 1024)
    private String errorReason;

    @Column(name = "send_date")
    private Instant sendDate;

    @Column(name = "create_date", nullable = false)
    private Instant createDate;

}