package com.bottari.teambottari.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import jakarta.persistence.Column;
import com.bottari.vo.ItemName;
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
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamPersonalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ItemName name;

    private boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_member_id")
    private TeamMember teamMember;

    // Soft Delete 경우에만 사용됨
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public TeamPersonalItem(
            final String name,
            final TeamMember teamMember
    ) {
        this.name = new ItemName(name);
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

    public String getName() {
        return name.name();
    }
}
