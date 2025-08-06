package com.bottari.support;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.log.LogFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthCheckController.class)
@Import(LogFormatter.class)
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("헬스 체크를 한다.")
    @Test
    void healthCheck() throws Exception {
        // when & then
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk());
    }
}
