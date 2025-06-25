package ru.stitchonfire.aitest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class AdvertisementPaymentId implements Serializable {
    @Serial
    private static final long serialVersionUID = -2649469748085485140L;
    @Column(name = "advertisement_id", nullable = false)
    private Integer advertisementId;

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AdvertisementPaymentId entity = (AdvertisementPaymentId) o;
        return Objects.equals(this.advertisementId, entity.advertisementId) &&
                Objects.equals(this.paymentId, entity.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(advertisementId, paymentId);
    }

}