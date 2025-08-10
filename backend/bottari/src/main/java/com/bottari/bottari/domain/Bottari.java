package com.bottari.bottari.domain;

import com.bottari.member.domain.Member;
import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@SQLDelete(sql = "UPDATE bottari SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("is_deleted = false")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bottari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Bottari(
            final String title,
            final Member member
    ) {
        validateTitle(title);
        this.title = title;
        this.member = member;
    }

    public boolean isOwner(final String ssaid) {
        return member.isSameBySsaid(ssaid);
    }

    public void updateTitle(final String newTitle) {
        if (title.equals(newTitle)) {
            throw new BusinessException(ErrorCode.BOTTARI_TITLE_UNCHANGED);
        }
        validateTitle(newTitle);
        this.title = newTitle;
    }

    private void validateTitle(final String title) {
        if (title.isBlank()) {
            throw new BusinessException(ErrorCode.BOTTARI_TITLE_BLANK);
        }
        if (title.length() > 15) {
            throw new BusinessException(ErrorCode.BOTTARI_TITLE_TOO_LONG, "최대 15자까지 입력 가능합니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Bottari bottari)) {
            return false;
        }

        return Objects.equals(getId(), bottari.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
