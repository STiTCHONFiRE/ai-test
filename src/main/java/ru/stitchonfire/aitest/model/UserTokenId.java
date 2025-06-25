package ru.stitchonfire.aitest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserTokenId implements Serializable {
    private static final long serialVersionUID = 278100879795148991L;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "login_provider", nullable = false, length = 128)
    private String loginProvider;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserTokenId entity = (UserTokenId) o;
        return Objects.equals(this.name, entity.name) &&
                Objects.equals(this.loginProvider, entity.loginProvider) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, loginProvider, userId);
    }

}