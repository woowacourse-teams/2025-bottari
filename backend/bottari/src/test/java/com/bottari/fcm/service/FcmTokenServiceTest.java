package com.bottari.fcm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.error.BusinessException;
import com.bottari.fcm.domain.FcmToken;
import com.bottari.fcm.dto.UpdateFcmRequest;
import com.bottari.fixture.FcmTokenFixture;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import jakarta.persistence.EntityManager;
import java.util.List;
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
    class GetByMemberIdTest {

        @DisplayName("멤버 ID로 FCM 토큰을 정상적으로 조회한다.")
        @Test
        void getByMemberId() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final FcmToken fcmToken = FcmTokenFixture.FCM_TOKEN.get(member);
            entityManager.persist(fcmToken);
            entityManager.flush();
            entityManager.clear();

            // when
            final FcmToken actual = fcmTokenService.getByMemberId(member.getId());

            // then
            assertThat(actual.getId()).isEqualTo(fcmToken.getId());
        }

        @DisplayName("멤버 ID로 조회 시, FCM 토큰이 존재하지 않으면 예외가 발생한다.")
        @Test
        void getByMemberId_Exception_NotFound() {
            // given
            final Long nonExistentMemberId = 999L;

            // when & then
            assertThatThrownBy(() -> fcmTokenService.getByMemberId(nonExistentMemberId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("FCM 토큰 정보가 존재하지 않습니다.");
        }

    }

    @Nested
    class GetByMembersInTest {

        @DisplayName("여러 멤버 ID로 FCM 토큰들을 정상적으로 조회한다.")
        @Test
        void getByMembersIn() {
            // given
            final Member member1 = MemberFixture.MEMBER.get();
            final Member member2 = MemberFixture.MEMBER.get();
            entityManager.persist(member1);
            entityManager.persist(member2);

            final FcmToken fcmToken1 = FcmTokenFixture.FCM_TOKEN.get(member1);
            final FcmToken fcmToken2 = FcmTokenFixture.FCM_TOKEN.get(member2);
            entityManager.persist(fcmToken1);
            entityManager.persist(fcmToken2);
            entityManager.flush();
            entityManager.clear();

            final List<Long> memberIds = List.of(member1.getId(), member2.getId());

            // when
            final List<FcmToken> actual = fcmTokenService.getByMembersIn(memberIds);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(2),
                    () -> assertThat(actual).extracting(FcmToken::getId)
                            .containsExactlyInAnyOrder(fcmToken1.getId(), fcmToken2.getId())
            );
        }

        @DisplayName("존재하지 않는 멤버 ID들로 조회하면 빈 리스트를 반환한다.")
        @Test
        void getByMembersIn_EmptyResult() {
            // given
            final List<Long> nonExistentMemberIds = List.of(999L, 1000L);

            // when
            final List<FcmToken> actual = fcmTokenService.getByMembersIn(nonExistentMemberIds);

            // then
            assertThat(actual).isEmpty();
        }

    }

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

    @Nested
    class DeleteByIdTest {

        @DisplayName("ID로 FCM 토큰을 정상적으로 삭제한다.")
        @Test
        void deleteById() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final FcmToken fcmToken = FcmTokenFixture.FCM_TOKEN.get(member);
            entityManager.persist(fcmToken);
            entityManager.flush();

            // when
            fcmTokenService.deleteById(fcmToken.getId());
            entityManager.flush();
            entityManager.clear();

            // then
            final FcmToken actual = entityManager.find(FcmToken.class, fcmToken.getId());
            assertThat(actual).isNull();
        }
    }

    @Nested
    class DeleteByIdsTest {

        @DisplayName("여러 ID로 FCM 토큰들을 정상적으로 삭제한다.")
        @Test
        void deleteByIds() {
            // given
            final Member member1 = MemberFixture.MEMBER.get();
            final Member member2 = MemberFixture.MEMBER.get();
            entityManager.persist(member1);
            entityManager.persist(member2);

            final FcmToken fcmToken1 = FcmTokenFixture.FCM_TOKEN.get(member1);
            final FcmToken fcmToken2 = FcmTokenFixture.FCM_TOKEN.get(member2);
            entityManager.persist(fcmToken1);
            entityManager.persist(fcmToken2);
            entityManager.flush();

            final List<Long> ids = List.of(fcmToken1.getId(), fcmToken2.getId());

            // when
            fcmTokenService.deleteByIds(ids);
            entityManager.flush();
            entityManager.clear();

            // then
            assertAll(
                    () -> assertThat(entityManager.find(FcmToken.class, fcmToken1.getId())).isNull(),
                    () -> assertThat(entityManager.find(FcmToken.class, fcmToken2.getId())).isNull()
            );
        }

        @DisplayName("빈 리스트로 삭제를 호출해도 예외가 발생하지 않는다.")
        @Test
        void deleteByIds_EmptyList() {
            // given
            final List<Long> emptyIds = List.of();

            // when & then
            fcmTokenService.deleteByIds(emptyIds);
        }
    }
}
