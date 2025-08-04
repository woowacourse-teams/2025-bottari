package com.bottari.dto;

public record CheckRegistrationResponse(
        boolean isRegistered,
        Long id,
        String name
) {
}
