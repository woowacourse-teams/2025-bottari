package com.bottari.push;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;

import com.bottari.config.FirebaseConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PushManagerTest {

    @MockitoBean
    private FirebaseConfig firebaseConfig;

    @MockitoBean
    private FirebaseMessaging messaging;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PushManager pushManager;

    @DisplayName("PushManager 인스턴스 생성 시 PushChannel 구현체 Bean이 List로 주입된다.")
    @Test
    void createInstance() {
        // given
        final Map<String, PushChannel> pushChannelBeans = applicationContext.getBeansOfType(PushChannel.class);
        final PushChannel[] expected = pushChannelBeans.values().toArray(new PushChannel[0]);

        // when & then
        assertThat(pushManager).extracting("pushChannels", MAP)
                .containsValues(expected);
    }
}
