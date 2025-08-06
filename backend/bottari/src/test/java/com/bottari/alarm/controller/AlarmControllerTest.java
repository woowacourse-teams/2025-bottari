package com.bottari.alarm.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.alarm.domain.RepeatType;
import com.bottari.alarm.dto.CreateAlarmRequest;
import com.bottari.alarm.dto.UpdateAlarmRequest;
import com.bottari.alarm.service.AlarmService;
import com.bottari.log.LogFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
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

@WebMvcTest(AlarmController.class)
@Import(LogFormatter.class)
class AlarmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlarmService alarmService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("알람을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        final Long bottariId = 1L;
        final CreateAlarmRequest request = new CreateAlarmRequest(
                new CreateAlarmRequest.RoutineAlarmRequest(
                        LocalTime.MAX,
                        RepeatType.EVERY_WEEK_REPEAT,
                        null,
                        List.of(1, 4, 7) // 월, 목, 일
                ),
                new CreateAlarmRequest.LocationAlarmRequest(
                        true,
                        1.23,
                        1.23,
                        100
                )
        );
        given(alarmService.create(bottariId, request))
                .willReturn(1L);

        // when & then
        mockMvc.perform(post("/bottaries/" + bottariId + "/alarms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/alarms/1"));
    }

    @DisplayName("알람을 수정한다.")
    @Test
    void update() throws Exception {
        // given
        final Long alarmId = 1L;
        final UpdateAlarmRequest request = new UpdateAlarmRequest(
                new UpdateAlarmRequest.RoutineAlarmRequest(
                        LocalTime.MAX,
                        RepeatType.EVERY_WEEK_REPEAT,
                        null,
                        List.of(1, 4, 7) // 월, 목, 일
                ),
                new UpdateAlarmRequest.LocationAlarmRequest(
                        true,
                        1.23,
                        1.23,
                        100
                )
        );
        willDoNothing().given(alarmService)
                .update(alarmId, request);

        // when & then
        mockMvc.perform(put("/alarms/" + alarmId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("알람을 활성화한다.")
    @Test
    void active() throws Exception {
        // given
        final Long alarmId = 1L;
        willDoNothing().given(alarmService)
                .active(alarmId);

        // when & then
        mockMvc.perform(patch("/alarms/" + alarmId + "/active"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("알람을 비활성화한다.")
    @Test
    void inactive() throws Exception {
        // given
        final Long alarmId = 1L;
        willDoNothing().given(alarmService)
                .inactive(alarmId);

        // when & then
        mockMvc.perform(patch("/alarms/" + alarmId + "/inactive"))
                .andExpect(status().isNoContent());
    }
}
