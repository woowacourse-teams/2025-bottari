package com.bottari.teambottari.dto;

import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import java.util.List;
import java.util.Map;

// TODO: 네이밍 다시 해보기
public record ReadTeamItemResponse(
        List<TeamBottariItemWithCheckResponse> sharedItems,
        List<TeamBottariItemWithCheckResponse> assignedItems
) {

    public static ReadTeamItemResponse from(
            final Map<TeamSharedItemInfo, List<TeamSharedItem>> sharedItems,
            final Map<TeamAssignedItemInfo, List<TeamAssignedItem>> assignedItems
    ) {
        final List<TeamBottariItemWithCheckResponse> sharedItemsResponses = sharedItems.keySet()
                .stream()
                .map(sharedItems::get)
                .map(TeamBottariItemWithCheckResponse::from1)
                .toList();
        final List<TeamBottariItemWithCheckResponse> assignedItemResponses = assignedItems.keySet()
                .stream()
                .map(assignedItems::get)
                .map(TeamBottariItemWithCheckResponse::from2)
                .toList();

        return new ReadTeamItemResponse(
                sharedItemsResponses,
                assignedItemResponses
        );
    }

    public record TeamBottariItemWithCheckResponse(
            String name,
            List<MemberCheckStatusResponse> memberCheckStatusResponses,
            int checkCount,
            int totalCount
    ) {

        public static TeamBottariItemWithCheckResponse from1(final List<TeamSharedItem> items) {
            final String itemName = items.getFirst()
                    .getInfo()
                    .getName();
            final List<MemberCheckStatusResponse> checkStatusResponses = items.stream()
                    .map(MemberCheckStatusResponse::from)
                    .toList();
            final int checkCount = Math.toIntExact(checkStatusResponses.stream()
                    .filter(MemberCheckStatusResponse::checked)
                    .count());

            return new TeamBottariItemWithCheckResponse(
                    itemName,
                    checkStatusResponses,
                    checkCount,
                    items.size()
            );
        }

        public static TeamBottariItemWithCheckResponse from2(final List<TeamAssignedItem> items) {
            final String itemName = items.getFirst()
                    .getInfo()
                    .getName();
            final List<MemberCheckStatusResponse> checkStatusResponses = items.stream()
                    .map(MemberCheckStatusResponse::from)
                    .toList();
            final int checkCount = Math.toIntExact(checkStatusResponses.stream()
                    .filter(MemberCheckStatusResponse::checked)
                    .count());

            return new TeamBottariItemWithCheckResponse(
                    itemName,
                    checkStatusResponses,
                    checkCount,
                    items.size()
            );
        }
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
