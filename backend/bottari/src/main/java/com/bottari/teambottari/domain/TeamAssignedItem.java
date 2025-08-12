package com.bottari.teambottari.domain;

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
public class TeamAssignedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_assigned_item_info_id")
    private TeamAssignedItemInfo info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_member_id")
    private TeamMember teamMember;

    public TeamAssignedItem(
            final TeamAssignedItemInfo info,
            final TeamMember teamMember
    ) {
        this.info = info;
        this.teamMember = teamMember;
        this.isChecked = false;
    }
}
