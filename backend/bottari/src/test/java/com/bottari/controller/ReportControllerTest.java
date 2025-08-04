package com.bottari.controller;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.dto.ReportBottariTemplateRequest;
import com.bottari.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("보따리 템플릿을 신고한다.")
    @Test
    void report() throws Exception {
        // given
        final Long bottariTemplateId = 1L;
        final String ssaid = "ssaid";
        final ReportBottariTemplateRequest request = new ReportBottariTemplateRequest("reason");
        willDoNothing().given(reportService)
                .reportBottariTemplate(ssaid, bottariTemplateId, request);

        // when & then
        mockMvc.perform(post("/reports/templates/" + bottariTemplateId)
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
