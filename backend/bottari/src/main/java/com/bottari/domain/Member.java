package com.bottari.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ssaid;

    private String name;

    public Member(
            final String ssaid,
            final String name
    ) {
        validateName(name);
        this.ssaid = ssaid;
        this.name = name;
    }

    private void validateName(final String name) {
        if (name.length() < 3 || name.length() > 10) {
            throw new IllegalArgumentException("사용자 이름은 3글자 이상 10글자 이하여야 합니다.");
        }
    }
}
