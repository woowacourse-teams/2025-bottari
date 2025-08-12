package com.bottari.teambottari.controller;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.config.WebConfig;
import com.bottari.log.LogFormatter;
import com.bottari.teambottari.service.TeamSharedItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TeamSharedItemController.class)
@Import({LogFormatter.class, WebConfig.class})
class TeamSharedItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamSharedItemService teamSharedItemService;

    @DisplayName("팀 공통 아이템을 체크한다.")
    @Test
    void check() throws Exception {
        // given
        final Long itemId = 1L;
        final String ssaid = "test_ssaid";

        willDoNothing().given(teamSharedItemService)
                .check(itemId, ssaid);

        // when & then
        mockMvc.perform(patch("/team-shared-items/{id}/check", itemId)
                        .header("ssaid", ssaid))
                .andExpect(status().isNoContent());
    }

    @DisplayName("팀 공통 아이템을 체크 해제한다.")
    @Test
    void uncheck() throws Exception {
        // given
        final Long itemId = 1L;
        final String ssaid = "test_ssaid";

        willDoNothing().given(teamSharedItemService)
                .uncheck(itemId, ssaid);

        // when & then
        mockMvc.perform(patch("/team-shared-items/{id}/uncheck", itemId)
                        .header("ssaid", ssaid))
                .andExpect(status().isNoContent());
    }
}
