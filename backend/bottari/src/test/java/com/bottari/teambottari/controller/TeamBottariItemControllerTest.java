package com.bottari.teambottari.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.config.WebConfig;
import com.bottari.log.LogFormatter;
import com.bottari.teambottari.domain.TeamItemType;
import com.bottari.teambottari.dto.CheckTeamItemRequest;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse.TeamMemberItemResponse;
import com.bottari.teambottari.service.TeamItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TeamBottariItemController.class)
@Import({LogFormatter.class, WebConfig.class})
class TeamBottariItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamItemService teamItemService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("팀 보따리 체크리스트를 성공적으로 조회한다.")
    @Test
    void readChecklistBySsaid() throws Exception {
        // given
        final Long teamBottariId = 1L;
        final String ssaid = "test-ssaid";

        final List<TeamMemberItemResponse> sharedItems = List.of(
                new TeamMemberItemResponse(1L, "공통 물품1", false),
                new TeamMemberItemResponse(2L, "공통 물품2", true)
        );
        final List<TeamMemberItemResponse> assignedItems = List.of(
                new TeamMemberItemResponse(3L, "담당 물품1", false)
        );
        final List<TeamMemberItemResponse> personalItems = List.of(
                new TeamMemberItemResponse(4L, "개인 물품1", true),
                new TeamMemberItemResponse(5L, "개인 물품2", false)
        );

        final TeamMemberChecklistResponse responses = new TeamMemberChecklistResponse(
                sharedItems,
                assignedItems,
                personalItems
        );

        given(teamItemService.getCheckList(teamBottariId, ssaid))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/teams/{teamBottariId}/checklist", teamBottariId)
                        .header("ssaid", ssaid))
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @DisplayName("팀 보따리 물품을 체크한다.")
    @ParameterizedTest
    @CsvSource({
            "SHARED",
            "ASSIGNED",
            "PERSONAL"
    })
    void check(final TeamItemType type) throws Exception {
        // given
        final Long itemId = 1L;
        final String ssaid = "test-ssaid";
        final CheckTeamItemRequest request = new CheckTeamItemRequest(type);

        willDoNothing().given(teamItemService)
                .check(itemId, ssaid, request);

        // when & then
        mockMvc.perform(patch("/team-items/{id}/check", itemId)
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("팀 보따리 물품을 체크 해제한다.")
    @ParameterizedTest
    @CsvSource({
            "SHARED",
            "ASSIGNED",
            "PERSONAL"
    })
    void uncheck(final TeamItemType type) throws Exception {
        // given
        final Long itemId = 1L;
        final String ssaid = "test-ssaid";
        final CheckTeamItemRequest request = new CheckTeamItemRequest(type);

        willDoNothing().given(teamItemService)
                .uncheck(itemId, ssaid, request);

        // when & then
        mockMvc.perform(patch("/team-items/{id}/uncheck", itemId)
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
