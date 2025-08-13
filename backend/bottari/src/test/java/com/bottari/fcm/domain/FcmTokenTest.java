package com.bottari.fcm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bottari.fixture.FcmTokenFixture;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FcmTokenTest {

    @DisplayName("정상적으로 Fcm 토큰 정보를 업데이트한다.")
    @Test
    void updateFcmToken() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        final FcmToken fcmToken = FcmTokenFixture.FCM_TOKEN.get(member);
        final String afterToken = "afterToken";

        // when
        fcmToken.updateFcmToken(afterToken);

        // then
        assertThat(fcmToken.getToken()).isEqualTo(afterToken);
    }
}