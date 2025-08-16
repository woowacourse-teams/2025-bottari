package com.bottari.member.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.support.BadWordValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_member_ssaid_active", columnNames = {"ssaid", "deleted_at"}),
                @UniqueConstraint(name = "UK_member_name_active", columnNames = {"name", "deleted_at"})
        }
)
@SQLDelete(sql = "UPDATE member SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ssaid;

    @Column(nullable = false)
    private String name;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

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
            throw new BusinessException(ErrorCode.MEMBER_NAME_UNCHANGED);
        }
        validateName(newName);
        this.name = newName;
    }

    private void validateName(final String name) {
        if (name.length() < 2) {
            throw new BusinessException(ErrorCode.MEMBER_NAME_TOO_SHORT, "최소 2자 이상 입력 가능합니다.");
        }
        if (name.length() > 10) {
            throw new BusinessException(ErrorCode.MEMBER_NAME_TOO_LONG, "최대 10자까지 입력 가능합니다.");
        }
        if (BadWordValidator.hasBadWord(name)) {
            throw new BusinessException(ErrorCode.MEMBER_NAME_OFFENSIVE);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Member member)) {
            return false;
        }
        if (getId() == null && member.getId() == null) {
            return Objects.equals(getSsaid(), member.getSsaid());
        }

        return Objects.equals(getId(), member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
