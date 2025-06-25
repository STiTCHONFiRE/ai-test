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
public class AnnouncementPaymentId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1617049153250121886L;
    @Column(name = "announcement_id", nullable = false)
    private Integer announcementId;

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AnnouncementPaymentId entity = (AnnouncementPaymentId) o;
        return Objects.equals(this.paymentId, entity.paymentId) &&
                Objects.equals(this.announcementId, entity.announcementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, announcementId);
    }

}