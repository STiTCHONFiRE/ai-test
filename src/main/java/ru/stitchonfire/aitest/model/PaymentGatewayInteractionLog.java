package ru.stitchonfire.aitest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "payment_gateway_interaction_log")
public class PaymentGatewayInteractionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "request_date", nullable = false)
    private Instant requestDate;

    @Column(name = "request_json", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> requestJson;

    @Column(name = "response_date", nullable = false)
    private Instant responseDate;

    @Column(name = "response_json", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> responseJson;

    @Column(name = "status_code", nullable = false)
    private Integer statusCode;

}