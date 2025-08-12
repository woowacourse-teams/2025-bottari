package com.bottari.teambottari.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamBottari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @Column(unique = true)
    private String inviteCode;

    @CreatedDate
    private LocalDateTime createdAt;

    public TeamBottari(
            final String title,
            final Member owner,
            final String inviteCode
    ) {
        validateTitle(title);
        this.title = title;
        this.owner = owner;
        this.inviteCode = inviteCode;
    }

    private void validateTitle(final String title) {
        if (title.isBlank()) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_TITLE_BLANK);
        }
        if (title.length() > 15) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_TITLE_TOO_LONG, "최대 15자까지 입력 가능합니다.");
        }
    }
}
