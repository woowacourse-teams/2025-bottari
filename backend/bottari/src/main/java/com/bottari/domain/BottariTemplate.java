package com.bottari.domain;

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
public class BottariTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int takenCount;

    @CreatedDate
    private LocalDateTime createdAt;

    public BottariTemplate(
            final String title,
            final Member member
    ) {
        validateTitle(title);
        this.title = title;
        this.member = member;
        this.takenCount = 0;
    }

    public boolean isOwner(final String ssaid) {
        return member.isSameBySsaid(ssaid);
    }

    private void validateTitle(final String title) {
        if (title.isBlank() || title.length() > 15) {
            throw new IllegalArgumentException("보따리 템플릿 이름은 공백이거나 15자를 넘을 수 없습니다.");
        }
    }
}
