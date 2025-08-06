package com.bottari.member.dto;

public record CheckRegistrationResponse(
        boolean isRegistered,
        Long id,
        String name
) {
}
