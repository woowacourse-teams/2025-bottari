package com.bottari.teambottari.domain;

import jakarta.persistence.Entity;
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
public class TeamSharedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isChecked;

    @ManyToOne
    @JoinColumn(name = "team_shared_item_info_id")
    private TeamSharedItemInfo info;

    @ManyToOne
    @JoinColumn(name = "team_member_id")
    private TeamMember teamMember;

    public TeamSharedItem(
            final TeamSharedItemInfo info,
            final TeamMember teamMember
    ) {
        this.info = info;
        this.teamMember = teamMember;
        this.isChecked = false;
    }
}
