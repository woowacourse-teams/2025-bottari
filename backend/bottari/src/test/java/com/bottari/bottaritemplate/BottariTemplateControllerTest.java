package com.bottari.bottaritemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.bottaritemplate.controller.BottariTemplateController;
import com.bottari.bottaritemplate.domain.SortProperty;
import com.bottari.bottaritemplate.dto.CreateBottariTemplateRequest;
import com.bottari.bottaritemplate.dto.ReadBottariTemplateResponse;
import com.bottari.bottaritemplate.dto.ReadBottariTemplateResponse.BottariTemplateItemResponse;
import com.bottari.bottaritemplate.service.BottariTemplateService;
import com.bottari.log.LogFormatter;
import com.bottari.bottaritemplate.dto.ReadNextBottariTemplateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(BottariTemplateController.class)
@Import(LogFormatter.class)
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

    @DisplayName("보따리 템플릿 목록을 페이징하여 조회한다.")
    @Test
    void readNextAll() throws Exception {
        // given
        final List<ReadBottariTemplateResponse> contents = List.of(
                new ReadBottariTemplateResponse(
                        1L,
                        "여행용 체크리스트",
                        List.of(
                                new BottariTemplateItemResponse(1L, "여권"),
                                new BottariTemplateItemResponse(2L, "항공권")
                        ),
                        "author_1",
                        LocalDateTime.now().minusDays(2),
                        5
                ),
                new ReadBottariTemplateResponse(
                        2L,
                        "캠핑 준비물",
                        List.of(
                                new BottariTemplateItemResponse(3L, "텐트")
                        ),
                        "author_2",
                        LocalDateTime.now().minusDays(1),
                        3
                )
        );
        final ReadNextBottariTemplateResponse response = new ReadNextBottariTemplateResponse(
                contents,
                0,
                2,
                true,
                true,
                false,
                SortProperty.CREATED_AT.getProperty(),
                2L,
                "2024-12-20T10:30:00Z"
        );
        given(bottariTemplateService.getNextAll(any()))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/templates/cursor")
                        .param("query", "")
                        .param("page", "0")
                        .param("size", "2")
                        .param("property", "createdAt"))
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
