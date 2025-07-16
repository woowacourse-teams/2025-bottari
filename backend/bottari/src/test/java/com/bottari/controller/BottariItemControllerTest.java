package com.bottari.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.dto.CreateBottariItemRequest;
import com.bottari.service.BottariItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BottariItemController.class)
class BottariItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BottariItemService bottariItemService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("보따리에 물품을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        final Long bottariId = 1L;
        final CreateBottariItemRequest request = new CreateBottariItemRequest("name");
        given(bottariItemService.create(bottariId, request))
                .willReturn(1L);

        // when & then
        mockMvc.perform(post("/bottaries/" + bottariId + "/bottari-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/bottaries/" + bottariId + "/bottari-items/1"));
    }

    @DisplayName("보따리 안에 있는 물품을 삭제한다.")
    @Test
    void delete_Item() throws Exception {
        // given
        final Long bottariItemId = 1L;
        willDoNothing().given(bottariItemService)
                .delete(bottariItemId);

        // when & then
        mockMvc.perform(delete("/bottari-items/" + bottariItemId))
                .andExpect(status().isNoContent());
    }

    @DisplayName("보따리 물품을 체크한다.")
    @Test
    void check() throws Exception {
        // given
        final Long bottariItemId = 1L;
        willDoNothing().given(bottariItemService)
                .check(bottariItemId);

        // when & then
        mockMvc.perform(patch("/bottari-items/" + bottariItemId + "/check"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("보따리 물품을 체크 해제한다.")
    @Test
    void uncheck() throws Exception {
        // given
        final Long bottariItemId = 1L;
        willDoNothing().given(bottariItemService)
                .uncheck(bottariItemId);

        // when & then
        mockMvc.perform(patch("/bottari-items/" + bottariItemId + "/uncheck"))
                .andExpect(status().isNoContent());
    }
}
