package com.bottari.bottari.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bottari.bottari.service.BottariBackupService;
import com.bottari.log.LogFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(BottariBackupController.class)
@Import(LogFormatter.class)
class BottariBackupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BottariBackupService bottariBackupService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("나의 보따리를 불러온다.")
    @Test
    void load() throws Exception {
        // given
        final String ssaid = "ssaid";
        final byte[] bytes = "backup-content".getBytes(StandardCharsets.UTF_8);
        final Resource resource = new ByteArrayResource(bytes);

        given(bottariBackupService.load(ssaid))
                .willReturn(resource);

        // when & then
        mockMvc.perform(get("/bottaries/load")
                        .header("ssaid", ssaid))
                .andExpect(status().isOk())
                .andExpect(content().bytes(bytes));
    }

    @DisplayName("나의 보따리를 백업한다.")
    @Test
    void backup() throws Exception {
        // given
        final String ssaid = "ssaid";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "backup.zip",
                "application/zip",
                "backup-content".getBytes()
        );

        given(bottariBackupService.backup(eq(ssaid), any(MultipartFile.class)))
                .willReturn("bottari/1/backup.csv");

        // when & then

        mockMvc.perform(multipart("/bottaries/backup")
                        .file(file)
                        .header("ssaid", ssaid)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(
                                Map.of("key", "bottari/1/backup.csv")
                        )
                ));
    }
}
