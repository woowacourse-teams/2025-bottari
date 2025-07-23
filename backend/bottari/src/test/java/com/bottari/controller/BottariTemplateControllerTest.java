package com.bottari.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.dto.CreateBottariTemplateRequest;
import com.bottari.dto.ReadBottariTemplateResponse;
import com.bottari.dto.ReadBottariTemplateResponse.BottariTemplateItemResponse;
import com.bottari.service.BottariTemplateService;
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
        given(bottariTemplateService.getAll(anyString()))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/templates"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @DisplayName("특정 보따리 템플릿을 조회한다.")
    @Test
    void read() throws Exception {
        // given
        final ReadBottariTemplateResponse response = new ReadBottariTemplateResponse(
                1L,
                "title_1",
                List.of(
                        new BottariTemplateItemResponse(1L, "item_1"),
                        new BottariTemplateItemResponse(2L, "item_2")
                ),
                "author_1");
        given(bottariTemplateService.getById(1L))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/templates/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("보따리 템플릿을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        final String ssaid = "ssaid";
        final List<String> bottariTemplateItems = List.of("item1", "item2");
        final CreateBottariTemplateRequest request = new CreateBottariTemplateRequest(
                "title",
                bottariTemplateItems
        );
        given(bottariTemplateService.create(ssaid, request))
                .willReturn(1L);

        // when & then
        mockMvc.perform(post("/templates")
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/templates/1"));
    }
}
