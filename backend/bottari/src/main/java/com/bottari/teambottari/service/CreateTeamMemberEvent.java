package com.bottari.teambottari.service;

public record CreateTeamMemberEvent(
        Long teamBottariId,
        Long memberId,
        String name,
        boolean isOwner
) {
}
