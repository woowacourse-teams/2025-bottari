package com.bottari.teambottari.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.config.WebConfig;
import com.bottari.log.LogFormatter;
import com.bottari.teambottari.domain.TeamItemType;
import com.bottari.teambottari.dto.CreateTeamAssignedItemRequest;
import com.bottari.teambottari.dto.CreateTeamItemRequest;
import com.bottari.teambottari.dto.ReadTeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamItemStatusResponse.MemberCheckStatusResponse;
import com.bottari.teambottari.dto.TeamItemTypeRequest;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.service.TeamItemFacade;
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
    private TeamItemFacade teamItemFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("팀 보따리 공통 물품을 성공적으로 생성한다.")
    @Test
    void createSharedItem() throws Exception {
        // given
        final Long teamBottariId = 1L;
        final String ssaid = "test-ssaid";
        final String itemName = "공통 물품1";

        final CreateTeamItemRequest request = new CreateTeamItemRequest(itemName);
        final Long createdItemId = 1L;

        given(teamItemFacade.createSharedItem(teamBottariId, request, ssaid))
                .willReturn(createdItemId);

        // when & then
        mockMvc.perform(post("/team-bottaries/{teamBottariId}/shared-items", teamBottariId)
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "/team-bottaries/" + teamBottariId + "/shared-items/" + createdItemId
                ));
    }

    @DisplayName("팀 보따리 담당 물품을 성공적으로 생성한다.")
    @Test
    void createAssignedItem() throws Exception {
        // given
        final Long teamBottariId = 1L;
        final String ssaid = "test-ssaid";
        final String itemName = "담당 물품1";

        final Long createdItemId = 1L;
        final CreateTeamAssignedItemRequest request = new CreateTeamAssignedItemRequest(
                itemName,
                List.of(1L, 2L)
        );

        given(teamItemFacade.createAssignedItem(teamBottariId, request, ssaid))
                .willReturn(createdItemId);

        // when & then
        mockMvc.perform(post("/team-bottaries/{teamBottariId}/assigned-items", teamBottariId)
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "/team-bottaries/" + teamBottariId + "/assigned-items/" + createdItemId
                ));
    }

    @DisplayName("팀 보따리 개인 물품을 성공적으로 생성한다.")
    @Test
    void createPersonalItem() throws Exception {
        // given
        final Long teamBottariId = 1L;
        final String ssaid = "test-ssaid";
        final String itemName = "개인 물품1";

        final CreateTeamItemRequest request = new CreateTeamItemRequest(itemName);
        final Long createdItemId = 1L;

        given(teamItemFacade.createPersonalItem(teamBottariId, request, ssaid))
                .willReturn(createdItemId);

        // when & then
        mockMvc.perform(post("/team-bottaries/{teamBottariId}/personal-items", teamBottariId)
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "/team-bottaries/" + teamBottariId + "/personal-items/" + createdItemId
                ));
    }

    @DisplayName("팀 보따리 물품을 성공적으로 삭제한다.")
    @ParameterizedTest
    @CsvSource({
            "SHARED",
            "PERSONAL"
    })
    void deleteItem(final TeamItemType type) throws Exception {
        // given
        final Long itemId = 1L;
        final String ssaid = "test-ssaid";
        final TeamItemTypeRequest request = new TeamItemTypeRequest(type);

        willDoNothing().given(teamItemFacade)
                .delete(itemId, ssaid, request);

        // when & then
        mockMvc.perform(delete("/team-items/{id}", itemId)
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("팀 보따리 담당 물품을 성공적으로 삭제한다.")
    @Test
    void deleteAssignedItem() throws Exception {
        // given
        final Long itemId = 1L;
        final String ssaid = "test-ssaid";
        final TeamItemTypeRequest request = new TeamItemTypeRequest(TeamItemType.ASSIGNED);

        willDoNothing().given(teamItemFacade)
                .delete(itemId, ssaid, request);

        // when & then
        mockMvc.perform(delete("/team-items/{id}", itemId)
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("팀 보따리 물품 현황을 성공적으로 조회한다.")
    @Test
    void readTeamItemsStatus() throws Exception {
        // given
        final Long teamBottariId = 1L;
        final String ssaid = "test-ssaid";

        final List<MemberCheckStatusResponse> sharedItemMemberStatus = List.of(
                new MemberCheckStatusResponse("다이스", true),
                new MemberCheckStatusResponse("방벨로", false),
                new MemberCheckStatusResponse("이오이", true)
        );
        final List<MemberCheckStatusResponse> assignedItemMemberStatus = List.of(
                new MemberCheckStatusResponse("다이스", false),
                new MemberCheckStatusResponse("방벨로", true)
        );

        final List<TeamItemStatusResponse> sharedItems = List.of(
                new TeamItemStatusResponse("잠옷", sharedItemMemberStatus, 2, 3),
                new TeamItemStatusResponse("세면도구", sharedItemMemberStatus, 2, 3)
        );
        final List<TeamItemStatusResponse> assignedItems = List.of(
                new TeamItemStatusResponse("가스 버너", assignedItemMemberStatus, 1, 2),
                new TeamItemStatusResponse("생수", assignedItemMemberStatus, 1, 2)
        );

        final ReadTeamItemStatusResponse response = new ReadTeamItemStatusResponse(sharedItems, assignedItems);

        given(teamItemFacade.getTeamItemStatus(teamBottariId, ssaid))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/team-bottaries/{teamBottariId}/items/status", teamBottariId)
                        .header("ssaid", ssaid))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

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

        given(teamItemFacade.getCheckList(teamBottariId, ssaid))
                .willReturn(responses);

        // when & then
        mockMvc.perform(get("/team-bottaries/{teamBottariId}/checklist", teamBottariId)
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
        final TeamItemTypeRequest request = new TeamItemTypeRequest(type);

        willDoNothing().given(teamItemFacade)
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
        final TeamItemTypeRequest request = new TeamItemTypeRequest(type);

        willDoNothing().given(teamItemFacade)
                .uncheck(itemId, ssaid, request);

        // when & then
        mockMvc.perform(patch("/team-items/{id}/uncheck", itemId)
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
