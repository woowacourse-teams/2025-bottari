package com.bottari.teambottari.dto;

import com.bottari.member.domain.Member;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import java.util.List;

public record ReadAssignedItemResponse(
        Long id,
        String name,
        List<Assignee> assignees
) {

    public static ReadAssignedItemResponse from(
            final
            TeamAssignedItemInfo teamAssignedItemInfo,
            final List<Member> members
    ) {
        return new ReadAssignedItemResponse(
                teamAssignedItemInfo.getId(),
                teamAssignedItemInfo.getName(),
                members.stream()
                        .map(Assignee::from)
                        .toList()
        );
    }

    public record Assignee(
            Long memberId,
            String name
    ) {

        public static Assignee from(final Member member) {
            return new Assignee(
                    member.getId(),
                    member.getName()
            );
        }
    }
}
