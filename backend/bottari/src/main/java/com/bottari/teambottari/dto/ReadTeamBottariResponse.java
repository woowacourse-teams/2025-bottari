package com.bottari.teambottari.dto;

import com.bottari.alarm.dto.AlarmResponse;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamPersonalItem;
import com.bottari.teambottari.domain.TeamSharedItem;
import java.util.List;

public record ReadTeamBottariResponse(
        Long id,
        String title,
        List<TeamItem> sharedItems,
        List<TeamItem> assignedItems,
        List<TeamItem> personalItems,
        AlarmResponse alarm
) {

    public static ReadTeamBottariResponse of(
            final TeamBottari teamBottari,
            final List<TeamSharedItem> sharedItems,
            final List<TeamAssignedItem> assignedItems,
            final List<TeamPersonalItem> personalItems,
            final AlarmResponse alarm
    ) {
        return new ReadTeamBottariResponse(
                teamBottari.getId(),
                teamBottari.getTitle(),
                sharedItems.stream().map(TeamItem::from).toList(),
                assignedItems.stream().map(TeamItem::from).toList(),
                personalItems.stream().map(TeamItem::from).toList(),
                alarm
        );
    }

    public record TeamItem(
            Long id,
            String name
    ) {

        public static TeamItem from(final TeamSharedItem teamSharedItem) {
            return new TeamItem(
                    teamSharedItem.getId(),
                    teamSharedItem.getInfo().getName()
            );
        }

        public static TeamItem from(final TeamAssignedItem teamAssignedItem) {
            return new TeamItem(
                    teamAssignedItem.getId(),
                    teamAssignedItem.getInfo().getName()
            );
        }

        public static TeamItem from(final TeamPersonalItem teamPersonalItem) {
            return new TeamItem(
                    teamPersonalItem.getId(),
                    teamPersonalItem.getName()
            );
        }
    }
}
