package com.bottari;

import com.bottari.config.FirebaseConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class BottariApplicationTests {

    @MockitoBean
    private FirebaseConfig firebaseConfig;

    @MockitoBean
    private FirebaseMessaging messaging;

    @Test
    void contextLoads() {
    }
}
