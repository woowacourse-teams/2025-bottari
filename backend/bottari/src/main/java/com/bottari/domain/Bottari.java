package com.bottari.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
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

    private void validateTitle(final String title) {
        if (title.isBlank() || title.length() > 15) {
            throw new IllegalArgumentException("보따리 이름은 공백이거나 15자를 넘을 수 없습니다.");
        }
    }
}
