package com.bottari.teambottari.dto;

import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import java.util.List;

public record TeamItemStatusResponse(
        String name,
        List<MemberCheckStatusResponse> memberCheckStatus,
        int checkItemsCount,
        int totalItemsCount
) {

    public static TeamItemStatusResponse of(
            final TeamSharedItemInfo info,
            final List<TeamSharedItem> items
    ) {
        final List<MemberCheckStatusResponse> checkStatusResponses = items.stream()
                .map(MemberCheckStatusResponse::from)
                .toList();
        final int checkCount = Math.toIntExact(
                checkStatusResponses.stream()
                        .filter(MemberCheckStatusResponse::checked)
                        .count()
        );

        return new TeamItemStatusResponse(
                info.getName(),
                checkStatusResponses,
                checkCount,
                items.size()
        );
    }

    public static TeamItemStatusResponse of(
            final TeamAssignedItemInfo info,
            final List<TeamAssignedItem> items
    ) {
        final List<MemberCheckStatusResponse> checkStatusResponses = items.stream()
                .map(MemberCheckStatusResponse::from)
                .toList();
        final int checkCount = Math.toIntExact(checkStatusResponses.stream()
                .filter(MemberCheckStatusResponse::checked)
                .count());

        return new TeamItemStatusResponse(
                info.getName(),
                checkStatusResponses,
                checkCount,
                items.size()
        );
    }

    public record MemberCheckStatusResponse(
            String name,
            boolean checked
    ) {

        public static MemberCheckStatusResponse from(
                final TeamSharedItem item
        ) {
            final String memberName = item.getTeamMember()
                    .getMember()
                    .getName();

            return new MemberCheckStatusResponse(
                    memberName,
                    item.isChecked()
            );
        }

        public static MemberCheckStatusResponse from(
                final TeamAssignedItem item
        ) {
            final String memberName = item.getTeamMember()
                    .getMember()
                    .getName();

            return new MemberCheckStatusResponse(
                    memberName,
                    item.isChecked()
            );
        }
    }
}
