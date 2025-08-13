package com.bottari.teambottari.domain;

import com.bottari.member.domain.Member;
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
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_bottari_id")
    private TeamBottari teamBottari;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public TeamMember(
            final TeamBottari teamBottari,
            final Member member
    ) {
        this.teamBottari = teamBottari;
        this.member = member;
    }

    public boolean isOwner() {
        return teamBottari.getOwner().equals(member);
    }

    public boolean isSameBySsaid(final String ssaid) {
        return member.isSameBySsaid(ssaid);
    }
}
