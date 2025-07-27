package com.bottari.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.dto.CreateBottariRequest;
import com.bottari.dto.ReadBottariPreviewResponse;
import com.bottari.dto.ReadBottariResponse;
import com.bottari.service.BottariService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(BottariController.class)
class BottariControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BottariService bottariService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("보따리를 조회한다.")
    @Test
    void read() throws Exception {
        // given
        final String ssaid = "ssaid";
        final ReadBottariResponse response = new ReadBottariResponse(1L, "title", List.of(), null);
        given(bottariService.getById(ssaid, 1L))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/bottaries/1")
                        .header("ssaid", ssaid))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("보따리 프리뷰 목록을 조회한다.")
    @Test
    void readPreviews() throws Exception {
        // given
        final String ssaid = "ssaid";
        final List<ReadBottariPreviewResponse> responses = List.of(
                new ReadBottariPreviewResponse(1L, "title1", 1, 0, null),
                new ReadBottariPreviewResponse(2L, "title2", 3, 2, null)
        );
        given(bottariService.getAllBySsaidSortedByLatest(ssaid))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/bottaries")
                        .header("ssaid", ssaid))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @DisplayName("보따리를 생성한다.")
    @Test
    void create() throws Exception {
        // given
        final String ssaid = "ssaid";
        final CreateBottariRequest request = new CreateBottariRequest("title");
        given(bottariService.create(ssaid, request))
                .willReturn(1L);

        // when & then
        mockMvc.perform(post("/bottaries")
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/bottaries/1"));
    }

    @DisplayName("보따리를 삭제한다.")
    @Test
    void delete() throws Exception {
        // given
        final Long bottariId = 1L;
        final String ssaid = "ssaid";
        willDoNothing().given(bottariService)
                .deleteById(bottariId, ssaid);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/bottaries/" + bottariId)
                        .header("ssaid", ssaid))
                .andExpect(status().isNoContent());
    }
}
