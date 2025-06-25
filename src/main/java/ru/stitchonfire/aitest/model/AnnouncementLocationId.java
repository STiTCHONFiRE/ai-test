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
public class AnnouncementLocationId implements Serializable {
    @Serial
    private static final long serialVersionUID = -2844167899099293631L;
    @Column(name = "announcement_id", nullable = false)
    private Integer announcementId;

    @Column(name = "location_id", nullable = false)
    private Integer locationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AnnouncementLocationId entity = (AnnouncementLocationId) o;
        return Objects.equals(this.locationId, entity.locationId) &&
                Objects.equals(this.announcementId, entity.announcementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, announcementId);
    }

}