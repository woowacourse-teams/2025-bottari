package com.bottari.fcm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

import com.bottari.error.BusinessException;
import com.bottari.fcm.domain.FcmToken;
import com.bottari.fcm.dto.MessageType;
import com.bottari.fcm.dto.SendMessageRequest;
import com.bottari.fcm.service.FcmTokenService;
import com.bottari.fixture.FcmTokenFixture;
import com.bottari.fixture.MemberFixture;
import com.bottari.member.domain.Member;
import com.google.firebase.ErrorCode;
import com.google.firebase.FirebaseException;
import com.google.firebase.IncomingHttpResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import jakarta.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DataJpaTest
@Import({
        FcmMessageSender.class,
        FcmTokenService.class
})
class FcmMessageSenderTest {

    @Autowired
    private FcmMessageSender fcmMessageSender;

    @Autowired
    private FcmTokenService fcmTokenService;

    @MockitoBean
    private FirebaseMessaging firebaseMessaging;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class sendMessageToMemberTest {

        @DisplayName("FCM 알림이 성공적으로 전송되면 예외가 발생하지 않는다.")
        @Test
        void sendMessageToMember() throws Exception {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final FcmToken fcmToken = FcmTokenFixture.FCM_TOKEN.get(member);
            entityManager.persist(fcmToken);
            doAnswer(invocation -> null)
                    .when(firebaseMessaging).send(any(Message.class));
            final SendMessageRequest request = new SendMessageRequest(Map.of(), MessageType.REMIND_BY_ITEM);

            // when & then
            assertThatCode(() -> fcmMessageSender.sendMessageToMember(member.getId(), request))
                    .doesNotThrowAnyException();
        }

        @DisplayName("FCM 토큰 정보가 존재하지 않으면 예외가 발생한다.")
        @Test
        void sendMessageToMember_Exception_NotExistsFcmToken() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final SendMessageRequest request = new SendMessageRequest(Map.of(), MessageType.REMIND_BY_ITEM);

            // when & then
            assertThatThrownBy(() -> fcmMessageSender.sendMessageToMember(member.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("FCM 토큰 정보가 존재하지 않습니다.");
        }

        @DisplayName("유효하지 않은 FCM 토큰을 사용하면, 해당 토큰을 비활성화하고 예외를 발생시킨다.")
        @ParameterizedTest
        @EnumSource(value = MessagingErrorCode.class, names = {"UNREGISTERED", "INVALID_ARGUMENT"})
        void sendMessageToMember_Exception_InvalidToken(final MessagingErrorCode errorCode)
                throws Exception {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final FcmToken fcmToken = FcmTokenFixture.FCM_TOKEN.get(member);
            entityManager.persist(fcmToken);
            final SendMessageRequest request = new SendMessageRequest(Map.of(), MessageType.REMIND_BY_ITEM);
            final FirebaseMessagingException exception = createFirebaseMessagingException(errorCode);
            doThrow(exception).when(firebaseMessaging).send(any(Message.class));

            // when & then
            assertThatThrownBy(() -> fcmMessageSender.sendMessageToMember(member.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("유효하지 않은 토큰으로 인해 FCM 메시지 전송을 실패하였습니다.");
            final FcmToken actual = (FcmToken) entityManager.createNativeQuery("""
                                  SELECT * FROM fcm_token WHERE member_id = :id
                            """, FcmToken.class)
                    .setParameter("id", member.getId())
                    .getSingleResult();
            assertThat(actual.getDeletedAt()).isNotNull();
        }

        @DisplayName("FCM 서버의 이상으로 메시지 전송에 실패하면 예외가 발생한다.")
        @ParameterizedTest
        @EnumSource(value = MessagingErrorCode.class, names = {"THIRD_PARTY_AUTH_ERROR", "INTERNAL", "QUOTA_EXCEEDED",
                "SENDER_ID_MISMATCH", "UNAVAILABLE"})
        void sendMessageToMember_Exception_FCMSeverProblem(final MessagingErrorCode errorCode)
                throws Exception {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final FcmToken fcmToken = FcmTokenFixture.FCM_TOKEN.get(member);
            entityManager.persist(fcmToken);
            final SendMessageRequest request = new SendMessageRequest(Map.of(), MessageType.REMIND_BY_ITEM);
            final FirebaseMessagingException exception = createFirebaseMessagingException(errorCode);
            doThrow(exception).when(firebaseMessaging).send(any(Message.class));

            // when & then
            assertThatThrownBy(() -> fcmMessageSender.sendMessageToMember(member.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("FCM 서버 문제로 FCM 메시지 전송을 실패하였습니다.");
        }
    }

    @Nested
    class sendMessageToMembersTest {

        @DisplayName("모든 사용자에게 FCM 알림이 성공적으로 전송되면 예외가 발생하지 않는다.")
        @Test
        void sendMessageToMembers() throws Exception {
            // given
            final Member member1 = MemberFixture.MEMBER.get();
            entityManager.persist(member1);
            final FcmToken fcmToken1 = FcmTokenFixture.FCM_TOKEN.get(member1);
            entityManager.persist(fcmToken1);
            final Member member2 = MemberFixture.MEMBER.get();
            entityManager.persist(member2);
            final FcmToken fcmToken2 = FcmTokenFixture.FCM_TOKEN.get(member2);
            entityManager.persist(fcmToken2);
            doAnswer(invocation -> null)
                    .when(firebaseMessaging).send(any(Message.class));
            final List<Long> memberIds = List.of(member1.getId(), member2.getId());
            final SendMessageRequest request = new SendMessageRequest(Map.of(), MessageType.REMIND_BY_ITEM);

            // when & then
            assertThatCode(() -> fcmMessageSender.sendMessageToMembers(memberIds, request))
                    .doesNotThrowAnyException();
        }

        @DisplayName("FCM 토큰 정보가 존재하지 않으면 예외가 발생한다.")
        @Test
        void sendMessageToMembers_Exception_NotExistsFcmToken() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final SendMessageRequest request = new SendMessageRequest(Map.of(), MessageType.REMIND_BY_ITEM);

            // when & then
            assertThatThrownBy(() -> fcmMessageSender.sendMessageToMember(member.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("FCM 토큰 정보가 존재하지 않습니다.");
        }

        @DisplayName("특정 사용자에 대해 유효하지 않은 FCM 토큰을 사용하면, 해당 토큰을 비활성화하고 예외를 발생시킨다.")
        @ParameterizedTest
        @EnumSource(value = MessagingErrorCode.class, names = {"UNREGISTERED", "INVALID_ARGUMENT"})
        void sendMessageToMembers_Exception_InvalidToken(final MessagingErrorCode errorCode) throws Exception {
            // given
            final Member member1 = MemberFixture.MEMBER.get();
            entityManager.persist(member1);
            final FcmToken fcmToken1 = FcmTokenFixture.FCM_TOKEN.get(member1);
            entityManager.persist(fcmToken1);
            final Member member2 = MemberFixture.MEMBER.get();
            entityManager.persist(member2);
            final FcmToken fcmToken2 = FcmTokenFixture.FCM_TOKEN.get(member2);
            entityManager.persist(fcmToken2);
            final FirebaseMessagingException exception = createFirebaseMessagingException(errorCode);
            final AtomicInteger counter = new AtomicInteger();
            doAnswer(invocation -> {
                final int call = counter.getAndIncrement();
                if (call == 0) {
                    throw exception;
                }
                return null;
            }).when(firebaseMessaging).send(any(Message.class));
            final List<Long> memberIds = List.of(member1.getId(), member2.getId());
            final SendMessageRequest request = new SendMessageRequest(Map.of(), MessageType.REMIND_BY_ITEM);

            // when & then
            assertThatThrownBy(() -> fcmMessageSender.sendMessageToMembers(memberIds, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("유효하지 않은 토큰으로 인해 FCM 메시지 전송을 실패하였습니다.");
            final FcmToken member1Token = (FcmToken) entityManager.createNativeQuery("""
                                SELECT * FROM fcm_token WHERE member_id = :id
                            """, FcmToken.class)
                    .setParameter("id", member1.getId())
                    .getSingleResult();
            final FcmToken member2Token = (FcmToken) entityManager.createNativeQuery("""
                                SELECT * FROM fcm_token WHERE member_id = :id
                            """, FcmToken.class)
                    .setParameter("id", member2.getId())
                    .getSingleResult();
            assertThat(member1Token.getDeletedAt()).isNotNull();
            assertThat(member2Token.getDeletedAt()).isNull();
        }

        @DisplayName("FCM 서버의 이상으로 메시지 전송에 실패하면 예외가 발생한다.")
        @ParameterizedTest
        @EnumSource(value = MessagingErrorCode.class, names = {"THIRD_PARTY_AUTH_ERROR", "INTERNAL", "QUOTA_EXCEEDED",
                "SENDER_ID_MISMATCH", "UNAVAILABLE"})
        void sendMessageToMembers_Exception_FCMSeverProblem(final MessagingErrorCode errorCode)
                throws Exception {
            // given
            final Member member1 = MemberFixture.MEMBER.get();
            entityManager.persist(member1);
            final FcmToken fcmToken1 = FcmTokenFixture.FCM_TOKEN.get(member1);
            entityManager.persist(fcmToken1);
            final Member member2 = MemberFixture.MEMBER.get();
            entityManager.persist(member2);
            final FcmToken fcmToken2 = FcmTokenFixture.FCM_TOKEN.get(member2);
            entityManager.persist(fcmToken2);
            final List<Long> memberIds = List.of(member1.getId(), member2.getId());
            final SendMessageRequest request = new SendMessageRequest(Map.of(), MessageType.REMIND_BY_ITEM);
            final FirebaseMessagingException exception = createFirebaseMessagingException(errorCode);
            doThrow(exception).when(firebaseMessaging).send(any(Message.class));

            // when & then
            assertThatThrownBy(() -> fcmMessageSender.sendMessageToMembers(memberIds, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("FCM 서버 문제로 FCM 메시지 전송을 실패하였습니다.");
        }
    }

    private FirebaseMessagingException createFirebaseMessagingException(final MessagingErrorCode errorCode)
            throws Exception {
        final FirebaseException firebaseException = new FirebaseException(ErrorCode.INTERNAL, "Invalid Token", null);
        final Constructor<FirebaseMessagingException> constructor =
                FirebaseMessagingException.class.getDeclaredConstructor(
                        ErrorCode.class,
                        String.class,
                        Throwable.class,
                        IncomingHttpResponse.class,
                        MessagingErrorCode.class
                );
        constructor.setAccessible(true);
        return constructor.newInstance(
                firebaseException.getErrorCode(),
                firebaseException.getMessage(),
                firebaseException.getCause(),
                firebaseException.getHttpResponse(),
                errorCode
        );
    }
}
