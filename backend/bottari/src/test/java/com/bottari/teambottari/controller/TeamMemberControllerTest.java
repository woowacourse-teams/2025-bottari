package com.bottari.teambottari.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.log.LogFormatter;
import com.bottari.teambottari.dto.JoinTeamBottariRequest;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
import com.bottari.teambottari.dto.ReadTeamMemberStatusResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.service.TeamMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TeamMemberController.class)
@Import(LogFormatter.class)
class TeamMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamMemberService teamMemberService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("팀원 관리 정보를 조회한다.")
    @Test
    void readTeamMemberManagementInfo() throws Exception {
        // given
        final Long teamBottariId = 1L;
        final String ssaid = "ssaid";
        final ReadTeamMemberInfoResponse response = new ReadTeamMemberInfoResponse(
                "Invite Code",
                3,
                "TeamMember1",
                List.of(
                        "TeamMember1",
                        "TeamMember2",
                        "TeamMember3"
                )
        );
        given(teamMemberService.getTeamMemberInfoByTeamBottariId(teamBottariId, ssaid))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/team-bottaries/{teamBottariId}/members", teamBottariId)
                        .header("ssaid", ssaid))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("팀원 별 챙김 현황 정보를 조회한다.")
    @Test
    void readTeamMemberStatus() throws Exception {
        // given
        final Long teamBottariId = 1L;
        final String ssaid = "ssaid";
        final List<ReadTeamMemberStatusResponse> response = List.of(
                new ReadTeamMemberStatusResponse(
                        "owner_name",
                        true,
                        2,
                        1,
                        List.of(
                                new TeamMemberItemResponse(1L, "shared_item_1", true),
                                new TeamMemberItemResponse(2L, "shared_item_2", false)
                        ),
                        List.of()
                ), new ReadTeamMemberStatusResponse(
                        "member_name",
                        false,
                        3,
                        2,
                        List.of(
                                new TeamMemberItemResponse(1L, "shared_item_1", false),
                                new TeamMemberItemResponse(2L, "shared_item_2", true)),
                        List.of(
                                new TeamMemberItemResponse(1L, "assigned_item_1", true)
                        )
                )
        );
        given(teamMemberService.getTeamMemberStatusByTeamBottariId(teamBottariId, ssaid))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/team-bottaries/{teamBottariId}/members/status", teamBottariId)
                        .header("ssaid", ssaid))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("멤버가 팀 보따리에 참가한다.")
    @Test
    void joinTeamBottari() throws Exception {
        // given
        final String ssaid = "ssaid";
        final Long teamMemberId = 1L;
        final JoinTeamBottariRequest request = new JoinTeamBottariRequest("inviteCode");
        given(teamMemberService.joinTeamBottari(request, ssaid))
                .willReturn(teamMemberId);

        // when & then
        mockMvc.perform(post("/team-bottaries/members/join")
                        .header("ssaid", ssaid)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/team-members/" + teamMemberId));
    }
}
