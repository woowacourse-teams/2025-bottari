package com.bottari.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
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
            throw new BusinessException(ErrorCode.MEMBER_NAME_IS_SAME);
        }
        validateName(newName);
        this.name = newName;
    }

    private void validateName(final String name) {
        if (name.length() < 3) {
            throw new BusinessException(ErrorCode.MEMBER_NAME_IS_SHORT, "최소 3자 이상 입력 가능합니다.");
        }
        if (name.length() > 10) {
            throw new BusinessException(ErrorCode.MEMBER_NAME_TOO_LONG, "최대 10자까지 입력 가능합니다.");
        }
    }
}
