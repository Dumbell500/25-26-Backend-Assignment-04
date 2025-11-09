package com.gdg.jwtexample.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "id_card")
@Getter
@NoArgsConstructor

public class IDCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String realName;

    @Column(nullable = false, length = 20)
    private String nationalId; // 주민번호(과제는 마스킹 or 단순 문자열)

    private LocalDate birth;
    private LocalDate issuedAt;

    public IDCard(User user, String realName, String nationalId, LocalDate birth, LocalDate issuedAt) {
        this.user = user;
        this.realName = realName;
        this.nationalId = nationalId;
        this.birth = birth;
        this.issuedAt = issuedAt;
    }

    public void update(String realName, String nationalId, LocalDate birth) {
        this.realName = realName;
        this.nationalId = nationalId;
        this.birth = birth;
    }
}
