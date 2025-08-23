package com.bottari.teambottari.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
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
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@Getter
public class TeamSharedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_shared_item_info_id")
    private TeamSharedItemInfo info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_member_id")
    private TeamMember teamMember;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public TeamSharedItem(
            final TeamSharedItemInfo info,
            final TeamMember teamMember
    ) {
        this.info = info;
        this.teamMember = teamMember;
        this.isChecked = false;
    }

    public void check() {
        if (isChecked) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_ALREADY_CHECKED, "공통");
        }
        this.isChecked = true;
    }

    public void uncheck() {
        if (!isChecked) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_ALREADY_UNCHECKED, "공통");
        }
        this.isChecked = false;
    }

    public boolean isOwner(final String ssaid) {
        return teamMember.isSameBySsaid(ssaid);
    }
}
