package ru.stitchonfire.aitest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class AdvertisementLocationId implements Serializable {
    @Serial
    private static final long serialVersionUID = 2033927624525868620L;

    @Column(name = "advertisement_id", nullable = false)
    private Integer advertisementId;

    @Column(name = "location_id", nullable = false)
    private Integer locationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AdvertisementLocationId entity = (AdvertisementLocationId) o;
        return Objects.equals(this.advertisementId, entity.advertisementId) &&
                Objects.equals(this.locationId, entity.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(advertisementId, locationId);
    }

}