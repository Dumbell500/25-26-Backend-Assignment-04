package com.gdg.jwtexample.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token", indexes = {
        @Index(name = "idx_refresh_token_token", columnList = "token", unique = true)
})
@Getter
@NoArgsConstructor

public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 512, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryAt;

    public RefreshToken(User user, String token, Instant expiryAt) {
        this.user = user;
        this.token = token;
        this.expiryAt = expiryAt;
    }

    public void rotate(String newToken, Instant newExpiry) {
        this.token = newToken;
        this.expiryAt = newExpiry;
    }
}
