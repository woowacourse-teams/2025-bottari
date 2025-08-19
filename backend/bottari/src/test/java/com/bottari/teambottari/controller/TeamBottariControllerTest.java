package com.bottari.teambottari.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.log.LogFormatter;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import com.bottari.teambottari.dto.ReadTeamBottariPreviewResponse;
import com.bottari.teambottari.dto.ReadTeamBottariResponse;
import com.bottari.teambottari.service.TeamBottariService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(TeamBottariController.class)
@Import(LogFormatter.class)
class TeamBottariControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamBottariService teamBottariService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("내가 소속된 팀 보따리 목록을 조회한다.")
    @Test
    void readPreviews() throws Exception {
        // given
        final String ssaid = "ssaid";
        final List<ReadTeamBottariPreviewResponse> response = List.of(
                new ReadTeamBottariPreviewResponse(1L, "title", 5, 3, 2, null),
                new ReadTeamBottariPreviewResponse(2L, "another title", 10, 7, 3, null)
        );
        given(teamBottariService.getAllBySsaid(ssaid))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/team-bottaries")
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("팀 보따리를 상세 조회한다.")
    @Test
    void read() throws Exception {
        // given
        final String ssaid = "ssaid";
        final Long teamBottariId = 1L;
        final ReadTeamBottariResponse response = new ReadTeamBottariResponse(
                teamBottariId,
                "title",
                List.of(),
                List.of(),
                List.of(),
                null
        );
        given(teamBottariService.getById(ssaid, teamBottariId))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/team-bottaries/{id}", teamBottariId)
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("팀 보따리를 생성한다.")
    @Test
    void create() throws Exception {
        // given
        final String ssaid = "ssaid";
        final CreateTeamBottariRequest request = new CreateTeamBottariRequest("title");
        given(teamBottariService.create(ssaid, request))
                .willReturn(1L);

        // when & then
        mockMvc.perform(post("/team-bottaries")
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/team-bottaries/1"));
    }
}
