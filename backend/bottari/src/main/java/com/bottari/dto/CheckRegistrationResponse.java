package com.bottari.dto;

public record CheckRegistrationResponse(
        boolean isRegistered,
        String name
) {
}
