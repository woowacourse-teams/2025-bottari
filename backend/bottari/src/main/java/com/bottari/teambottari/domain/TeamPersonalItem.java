package com.bottari.teambottari.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
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
public class TeamPersonalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_member_id")
    private TeamMember teamMember;

    public TeamPersonalItem(
            final String name,
            final TeamMember teamMember
    ) {
        validateName(name);
        this.name = name;
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

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NAME_BLANK);
        }
        if (name.length() > 20) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NAME_TOO_LONG, "최대 20자까지 입력 가능합니다.");
        }
    }
}
