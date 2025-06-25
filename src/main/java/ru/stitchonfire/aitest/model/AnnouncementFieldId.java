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
public class AnnouncementFieldId implements Serializable {
    @Serial
    private static final long serialVersionUID = 9182125658876780555L;
    @Column(name = "announcement_id", nullable = false)
    private Long announcementId;

    @Column(name = "category_field_id", nullable = false)
    private Integer categoryFieldId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AnnouncementFieldId entity = (AnnouncementFieldId) o;
        return Objects.equals(this.announcementId, entity.announcementId) &&
                Objects.equals(this.categoryFieldId, entity.categoryFieldId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(announcementId, categoryFieldId);
    }

}