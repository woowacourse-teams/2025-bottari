package com.bottari.teambottari.domain;

import com.bottari.member.domain.Member;
import com.bottari.support.BaseTimeEntity;
import com.bottari.vo.BottariTitle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE team_bottari SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamBottari extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BottariTitle title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @Column(unique = true)
    private String inviteCode;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public TeamBottari(
            final String title,
            final Member owner,
            final String inviteCode
    ) {
        this.title = new BottariTitle(title);
        this.owner = owner;
        this.inviteCode = inviteCode;
    }

    public boolean isOwner(final Member member) {
        return owner.equals(member);
    }

    public void changeOwner(final Member member) {
        owner = member;
    }

    public String getTitle() {
        return title.value();
    }
}
