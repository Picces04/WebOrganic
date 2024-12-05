package com.projectweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "PASSWORD_RESET_TOKENS")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "TOKEN", nullable = false)
    private String token;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CREATED_AT")
    private Instant createdAt;

    @Column(name = "EXPIRES_AT")
    private LocalDateTime expiresAt;

    public PasswordResetToken(String email, String token, LocalDateTime expiresAt) {
        this.email = email;
        this.token = token;
        this.expiresAt = expiresAt;
    }


    public PasswordResetToken() {

    }
}