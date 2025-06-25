package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "payment_system_payment_id", length = 127)
    private String paymentSystemPaymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "authorization_date")
    private Instant authorizationDate;

    @Column(name = "capture_date")
    private Instant captureDate;

    @Column(name = "fail_date")
    private Instant failDate;

    @Column(name = "description", nullable = false, length = 511)
    private String description;

    @Column(name = "return_url", nullable = false, length = 511)
    private String returnUrl;

    @Column(name = "money_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal moneyAmount;

    @Column(name = "money_currency_code", nullable = false, length = 7)
    private String moneyCurrencyCode;

    @Column(name = "money_currency_number", nullable = false)
    private Integer moneyCurrencyNumber;

    @Column(name = "money_currency_minor_units_ratio", nullable = false)
    private Integer moneyCurrencyMinorUnitsRatio;

    @Column(name = "used_card_holder_name", length = 254)
    private String usedCardHolderName;

    @Column(name = "used_card_masked_pan", length = 31)
    private String usedCardMaskedPan;

    @Column(name = "used_card_valid_thru")
    private LocalDate usedCardValidThru;

    @Column(name = "back_button_title", length = 200)
    private String backButtonTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @ManyToMany(mappedBy = "payments")
    private Set<Advertisement> advertisements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "payment")
    private Set<AnnouncementPayment> announcementPayments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "payment")
    private Set<PaymentGatewayInteractionLog> paymentGatewayInteractionLogs = new LinkedHashSet<>();

}