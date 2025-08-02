package com.bottari.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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
import java.time.LocalDateTime;
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

@WebMvcTest(BottariTemplateController.class)
class BottariTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BottariTemplateService bottariTemplateService;

    @Autowired
    private ObjectMapper objectMapper;

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
                "author_1",
                LocalDateTime.now(),
                0
        );
        given(bottariTemplateService.getById(1L))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/templates/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("내 보따리 템플릿을 조회한다.")
    @Test
    void readMine() throws Exception {
        // given
        final String ssaid = "ssaid";
        final List<ReadBottariTemplateResponse> responses = List.of(
                new ReadBottariTemplateResponse(
                        1L,
                        "title_1",
                        List.of(
                                new BottariTemplateItemResponse(1L, "item_1"),
                                new BottariTemplateItemResponse(2L, "item_2")
                        ),
                        "author_1",
                        LocalDateTime.now(),
                        0
                ),
                new ReadBottariTemplateResponse(
                        2L,
                        "title_2",
                        List.of(
                                new BottariTemplateItemResponse(3L, "item_3")
                        ),
                        "author_2",
                        LocalDateTime.now(),
                        0
                )
        );
        given(bottariTemplateService.getBySsaid("ssaid"))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/templates/me")
                        .header("ssaid", ssaid))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

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
                        "author_1",
                        LocalDateTime.now(),
                        0
                ),
                new ReadBottariTemplateResponse(
                        2L,
                        "title_2",
                        List.of(
                                new BottariTemplateItemResponse(3L, "item_3")
                        ),
                        "author_2",
                        LocalDateTime.now(),
                        0
                )
        );
        given(bottariTemplateService.getAll(anyString()))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/templates"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @DisplayName("검색어를 통해 보따리 템플릿을 조회한다.")
    @Test
    void readAll_WithQuery() throws Exception {
        // given
        final String query = "여행";
        final List<ReadBottariTemplateResponse> responses = List.of(
                new ReadBottariTemplateResponse(
                        1L,
                        "여행용 체크리스트",
                        List.of(
                                new BottariTemplateItemResponse(1L, "여권"),
                                new BottariTemplateItemResponse(2L, "항공권")
                        ),
                        "author_1",
                        LocalDateTime.now(),
                        5
                )
        );
        given(bottariTemplateService.getAll(query))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/templates")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @DisplayName("빈 검색어로 조회 시, 모든 보따리 템플릿을 최신순으로 조회한다.")
    @Test
    void readAll_WithEmptyQuery() throws Exception {
        // given
        final List<ReadBottariTemplateResponse> responses = List.of(
                new ReadBottariTemplateResponse(
                        1L,
                        "캠핑 준비물",
                        List.of(
                                new BottariTemplateItemResponse(1L, "텐트"),
                                new BottariTemplateItemResponse(2L, "침낭")
                        ),
                        "author_1",
                        LocalDateTime.now().minusDays(2),
                        3
                ),
                new ReadBottariTemplateResponse(
                        2L,
                        "출장 체크리스트",
                        List.of(
                                new BottariTemplateItemResponse(3L, "노트북")
                        ),
                        "author_2",
                        LocalDateTime.now().minusDays(1),
                        1
                )
        );
        given(bottariTemplateService.getAll(anyString()))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/templates")
                        .param("query", ""))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @DisplayName("검색 결과가 없다면, 빈 목록을 반환한다.")
    @Test
    void readAll_WithNoResults() throws Exception {
        // given
        final String query = "존재하지않는검색어";
        final List<ReadBottariTemplateResponse> responses = List.of();
        given(bottariTemplateService.getAll(query))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/templates")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
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

    @DisplayName("보따리 템플릿으로 보따리를 생성한다.")
    @Test
    void createBottari() throws Exception {
        // given
        final Long id = 2L;
        final String ssaid = "ssaid";
        given(bottariTemplateService.createBottari(id, ssaid))
                .willReturn(1L);

        // when & then
        mockMvc.perform(post("/templates/" + id + "/create-bottari")
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/bottaries/1"));
    }

    @DisplayName("보따리를 삭제한다.")
    @Test
    void delete() throws Exception {
        // given
        final String ssaid = "ssaid";
        final Long id = 1L;
        willDoNothing().given(bottariTemplateService)
                .deleteById(id, ssaid);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/templates/" + id)
                        .header("ssaid", ssaid))
                .andExpect(status().isNoContent());
    }
}
