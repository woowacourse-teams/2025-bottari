package com.bottari.teambottari.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.log.LogFormatter;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
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
        final String ssaid = "ssaid";
        final Long teamBottariId = 1L;
        final ReadTeamMemberInfoResponse response = new ReadTeamMemberInfoResponse(
                "Invite Code",
                3,
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
}
