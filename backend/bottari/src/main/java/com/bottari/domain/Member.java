package com.bottari.domain;

import jakarta.persistence.Column;
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

    @Column(unique = true)
    private String ssaid;

    @Column(unique = true)
    private String name;

    public Member(
            final String ssaid,
            final String name
    ) {
        validateName(name);
        this.ssaid = ssaid;
        this.name = name;
    }

    public boolean isSameBySsaid(final String ssaid) {
        return this.ssaid.equals(ssaid);
    }

    public void updateName(final String newName) {
        if (name.equals(newName)) {
            throw new IllegalArgumentException("기존 이름과 동일한 이름으로는 변경할 수 없습니다.");
        }
        validateName(newName);
        this.name = newName;
    }

    private void validateName(final String name) {
        if (name.length() < 3 || name.length() > 10) {
            throw new IllegalArgumentException("사용자 이름은 3글자 이상 10글자 이하여야 합니다.");
        }
    }
}
