package com.bottari.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.dto.ReadBottariTemplateResponse;
import com.bottari.dto.ReadBottariTemplateResponse.BottariTemplateItemResponse;
import com.bottari.service.BottariTemplateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BottariTemplateController.class)
class BottariTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BottariTemplateService bottariTemplateService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("모든 보따리 템플릿을 조회한다.")
    @Test
    void readAll() throws Exception {
        // given
        final List<ReadBottariTemplateResponse> responses = List.of(
                new ReadBottariTemplateResponse(
                        1L,
                        "title_1",
                        List.of(
                                new BottariTemplateItemResponse(1L, "item_1"),
                                new BottariTemplateItemResponse(2L, "item_2")
                        ),
                        "author_1"),
                new ReadBottariTemplateResponse(
                        2L,
                        "title_2",
                        List.of(
                                new BottariTemplateItemResponse(3L, "item_3")
                        ),
                        "author_2")
        );
        given(bottariTemplateService.getAll())
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/templates"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }
}
