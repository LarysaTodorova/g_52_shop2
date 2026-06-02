package de.aittr.g_52_shop2.domain.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "confirmation_code")
public class ConfirmationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "expiration")
    private LocalDateTime expiration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ConfirmationCode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmationCode that = (ConfirmationCode) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(expiration, that.expiration) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, expiration, user);
    }

    @Override
    public String toString() {
        return String.format(
                "Confirmation Code: id - %d, value - %s, expiration - %s, user email - %s",
                id,
                value,
                expiration == null ? "unknown" : expiration,
                user == null ? "unknown" : user.getEmail()
        );
    }
}
