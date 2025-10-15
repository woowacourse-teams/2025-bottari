package com.bottari.bottaritemplate.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.bottaritemplate.dto.ReadHashtagWithUsageCountResponse;
import com.bottari.bottaritemplate.service.HashtagService;
import com.bottari.log.LogFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HashtagController.class)
@Import(LogFormatter.class)
class HashtagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HashtagService hashtagService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("인기 해시태그를 조회한다.")
    @Test
    void readPopularHashtags() throws Exception {
        // given
        final List<ReadHashtagWithUsageCountResponse> responses = List.of(
                new ReadHashtagWithUsageCountResponse(1L, "여행", 42),
                new ReadHashtagWithUsageCountResponse(2L, "캠핑", 35),
                new ReadHashtagWithUsageCountResponse(3L, "등산", 28)
        );
        given(hashtagService.getTopHashtagsByUsageCount(10))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/hashtags/popular")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @DisplayName("limit 파라미터 없이 조회 시 기본값으로 조회한다.")
    @Test
    void readPopularHashtags_DefaultLimit() throws Exception {
        // given
        final List<ReadHashtagWithUsageCountResponse> responses = List.of(
                new ReadHashtagWithUsageCountResponse(1L, "여행", 42)
        );
        given(hashtagService.getTopHashtagsByUsageCount(10))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/hashtags/popular"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @DisplayName("limit 파라미터로 조회 개수를 지정할 수 있다.")
    @Test
    void readPopularHashtags_WithLimit() throws Exception {
        // given
        final List<ReadHashtagWithUsageCountResponse> responses = List.of(
                new ReadHashtagWithUsageCountResponse(1L, "여행", 42),
                new ReadHashtagWithUsageCountResponse(2L, "캠핑", 35)
        );
        given(hashtagService.getTopHashtagsByUsageCount(2))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/hashtags/popular")
                        .param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }
}
