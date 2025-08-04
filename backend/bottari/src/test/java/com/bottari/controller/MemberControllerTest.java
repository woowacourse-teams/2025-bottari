package com.bottari.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.dto.CheckRegistrationResponse;
import com.bottari.dto.CreateMemberRequest;
import com.bottari.dto.UpdateMemberRequest;
import com.bottari.log.LogFormatter;
import com.bottari.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@Import(LogFormatter.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("사용자를 생성한다.")
    @Test
    void register() throws Exception {
        // given
        final CreateMemberRequest request = new CreateMemberRequest("ssaid");
        given(memberService.create(request))
                .willReturn(1L);

        // when & then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/members/1"));
    }

    @DisplayName("사용자의 회원가입 여부를 확인한다.")
    @Test
    void checkRegistration() throws Exception {
        // given
        final String ssaid = "ssaid";
        final CheckRegistrationResponse response = new CheckRegistrationResponse(
                true,
                1L,
                "name"
        );
        given(memberService.checkRegistration(ssaid))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/members/check")
                        .header("ssaid", ssaid))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("사용자의 이름을 수정한다.")
    @Test
    void updateName() throws Exception {
        // given
        final String ssaid = "ssaid";
        final UpdateMemberRequest request = new UpdateMemberRequest("new_name");

        // when & then
        mockMvc.perform(patch("/members/me")
                        .header("ssaid", ssaid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
