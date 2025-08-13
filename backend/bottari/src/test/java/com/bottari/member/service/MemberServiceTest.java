package com.bottari.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.error.BusinessException;
import com.bottari.fcm.domain.FcmToken;
import com.bottari.member.domain.Member;
import com.bottari.member.dto.CheckRegistrationResponse;
import com.bottari.member.dto.CreateMemberRequest;
import com.bottari.member.dto.UpdateMemberRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(MemberService.class)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class CreateTest {

        @DisplayName("사용자를 생성한다.")
        @Test
        void create() {
            // given
            final CreateMemberRequest request = new CreateMemberRequest("ssaid", "token");

            // when
            final Long actual = memberService.create(request);

            // then
            assertThat(actual).isNotNull();
        }

        @DisplayName("사용자를 생성하면 FCM 토큰 또한 함께 생성된다.")
        @Test
        void create_WithFcmToken() {
            // given
            final CreateMemberRequest request = new CreateMemberRequest("ssaid", "token");

            // when
            final Long actual = memberService.create(request);

            // then
            final FcmToken actualFcmToken = entityManager.createQuery("""
                          SELECT f FROM FcmToken f WHERE f.member.id = :memberId
                    """, FcmToken.class)
                    .setParameter("memberId", actual)
                    .getSingleResult();
            assertThat(actualFcmToken).isNotNull();
        }

        @DisplayName("사용자를 생성하면 고유한 임시 이름이 부여된다.")
        @Test
        void create_AssignTemporaryName() {
            // given
            final String ssaid = "ssaid";
            final CreateMemberRequest request = new CreateMemberRequest(ssaid, "token");

            // when
            final Long memberId = memberService.create(request);

            // then
            final Member createdMember = entityManager.find(Member.class, memberId);
            assertAll(
                    () -> assertThat(createdMember).isNotNull(),
                    () -> assertThat(createdMember.getName()).isNotBlank()
            );
        }

        @DisplayName("중복된 ssaid로 사용자를 생성할 경우, 예외를 던진다.")
        @Test
        void create_Exception_DuplicateSsaid() {
            // given
            final String duplicateSsaid = "duplicateSsaid";
            entityManager.persist(new Member(duplicateSsaid, "name"));
            final CreateMemberRequest request = new CreateMemberRequest(duplicateSsaid, "token");

            // when & then
            assertThatThrownBy(() -> memberService.create(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("이미 사용 중인 ssaid입니다.");
        }
    }

    @Nested
    class CheckRegistrationTest {

        @DisplayName("사용자 회원가입 여부를 확인할 때 가입된 ssaid라면, true와 사용자 이름을 반환한다.")
        @Test
        void checkRegistration() {
            // given
            final String ssaid = "ssaid";
            final String name = "name";
            final Member member = new Member(ssaid, name);
            entityManager.persist(member);

            // when
            final CheckRegistrationResponse actual = memberService.checkRegistration(ssaid);

            // then
            assertAll(() -> {
                assertThat(actual.isRegistered()).isTrue();
                assertThat(actual.id()).isEqualTo(member.getId());
                assertThat(actual.name()).isEqualTo(name);
            });
        }

        @DisplayName("사용자 회원가입 여부를 확인할 때 가입되지 않은 ssaid라면, false와 null을 반환한다.")
        @Test
        void checkRegistration_unregister() {
            // given
            final String unregisteredSsaid = "ssaid";

            // when
            final CheckRegistrationResponse actual = memberService.checkRegistration(unregisteredSsaid);

            // then
            assertAll(() -> {
                assertThat(actual.isRegistered()).isFalse();
                assertThat(actual.id()).isNull();
                assertThat(actual.name()).isNull();
            });
        }
    }

    @Nested
    class UpdateNameTest {

        @DisplayName("사용자의 이름을 수정한다.")
        @Test
        void updateName() {
            // given
            final String ssaid = "ssaid";
            final Member member = new Member(ssaid, "name");
            entityManager.persist(member);

            final UpdateMemberRequest request = new UpdateMemberRequest("new_name");

            // when
            memberService.updateName(ssaid, request);

            // then
            final Member updatedMember = entityManager.find(Member.class, member.getId());
            final String actual = updatedMember.getName();
            assertThat(actual).isEqualTo("new_name");
        }

        @DisplayName("존재하지 않는 ssaid로 사용자의 이름을 수정할 경우, 예외를 던진다.")
        @Test
        void updateName_Exception_NotExistsSsaid() {
            // given
            final String ssaid = "invalid_ssaid";
            final UpdateMemberRequest request = new UpdateMemberRequest("new_name");

            // when & then
            assertThatThrownBy(() -> memberService.updateName(ssaid, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }

        @DisplayName("이미 사용 중인 이름으로 사용자의 이름을 수정할 경우, 예외를 던진다.")
        @Test
        void updateName_Exception_DuplicateName() {
            // given
            final String duplicatedName = "중복_이름";
            final Member memberWithDuplicatedName = new Member("ssaid_1", duplicatedName);
            entityManager.persist(memberWithDuplicatedName);

            final String requesterSsaid = "ssaid_2";
            final Member memberToUpdate = new Member(requesterSsaid, "기존_이름");
            entityManager.persist(memberToUpdate);

            final UpdateMemberRequest updateRequest = new UpdateMemberRequest(duplicatedName);

            // when & then
            assertThatThrownBy(() -> memberService.updateName(requesterSsaid, updateRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("이미 사용 중인 이름입니다.");
        }
    }
}
