package com.bottari.fcm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.error.BusinessException;
import com.bottari.fcm.domain.FcmToken;
import com.bottari.fcm.dto.UpdateFcmRequest;
import com.bottari.fixture.FcmTokenFixture;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(FcmTokenService.class)
class FcmTokenServiceTest {

    @Autowired
    private FcmTokenService fcmTokenService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class UpdateTest {

        @DisplayName("토큰을 정상적으로 업데이트한다.")
        @Test
        void updateFcmToken() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final FcmToken fcmToken = FcmTokenFixture.FCM_TOKEN.get(member);
            entityManager.persist(fcmToken);
            final String afterToken = "afterToken";
            final UpdateFcmRequest request = new UpdateFcmRequest(afterToken);

            // when
            fcmTokenService.updateFcmToken(member.getSsaid(), request);

            // then
            final FcmToken actual = entityManager.find(FcmToken.class, fcmToken.getId());
            assertThat(actual.getToken()).isEqualTo(afterToken);
        }

        @DisplayName("업데이트 시, 특정 ssaid에 대해 fcm 토큰이 존재하지 않는다면 예외가 발생한다.")
        @Test
        void updateFcmToken_Exception_NotExistsFcmToken() {
            // given
            final String ssaid = "ssaid";
            final String afterToken = "afterToken";
            final UpdateFcmRequest request = new UpdateFcmRequest(afterToken);

            // when & then
            assertThatThrownBy(() -> fcmTokenService.updateFcmToken(ssaid, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("FCM 토큰 정보가 존재하지 않습니다.");
        }
    }

}