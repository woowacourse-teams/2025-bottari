package com.bottari.push.connection.sse;

import com.bottari.config.MemberIdentifier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseConnector implements SseConnectorApiDocs {

    private final SseService sseService;

    @GetMapping(path = "/connect/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Override
    public SseEmitter connect(
            @MemberIdentifier final String ssaid
    ) {
        final long timeout = 60 * 60 * 1000L;
        final SseEmitter sseEmitter = new SseEmitter(timeout);
        sseService.register(ssaid, sseEmitter);

        return sseEmitter;
    }
}
