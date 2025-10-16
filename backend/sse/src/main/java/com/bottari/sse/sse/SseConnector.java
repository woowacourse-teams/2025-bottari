package com.bottari.sse.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseConnector implements SseConnectorApiDocs {

    private final SseService sseService;

    @GetMapping(path = "/connect/sse/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Override
    public SseEmitter connect(
            @PathVariable final Long memberId
    ) {
        final long timeout = 60 * 60 * 1000L;
        final SseEmitter sseEmitter = new SseEmitter(timeout);
        sseService.register(memberId, sseEmitter);

        return sseEmitter;
    }
}
