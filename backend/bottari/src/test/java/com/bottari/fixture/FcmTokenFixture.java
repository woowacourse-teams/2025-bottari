package com.bottari.fixture;

import com.bottari.fcm.domain.FcmToken;
import com.bottari.member.domain.Member;

public enum FcmTokenFixture {
    FCM_TOKEN("fcm_token");

    private final String token;

    FcmTokenFixture(final String token) {
        this.token = token;
    }

    public FcmToken get(final Member member) {
        return new FcmToken(token, member);
    }
}
