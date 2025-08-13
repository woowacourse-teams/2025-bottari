package com.bottari.fcm.controller;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.fcm.dto.UpdateFcmRequest;
import com.bottari.fcm.service.FcmTokenService;
import com.bottari.log.LogFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FcmTokenController.class)
@Import(LogFormatter.class)
class FcmTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FcmTokenService fcmTokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("fcm 토큰을 업데이트한다.")
    @Test
    void updateFcmToken() throws Exception {
        // given
        final String ssaid = "ssaid";
        final UpdateFcmRequest request = new UpdateFcmRequest("fcmToken");
        willDoNothing().given(fcmTokenService)
                .updateFcmToken(ssaid, request);

        // when & then
        mockMvc.perform(patch("/fcm")
                                .header("ssaid", ssaid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
