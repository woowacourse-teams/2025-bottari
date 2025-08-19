package com.bottari.sse.component;

import com.bottari.sse.message.SseEventType;
import com.bottari.sse.message.SseMessage;
import com.bottari.sse.message.SseResourceType;
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

    @GetMapping(path = "/team-bottaries/{teamBottariId}/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Override
    public SseEmitter connectTeamBottari(
            @PathVariable final Long teamBottariId
    ) {
        final long timeout = 60 * 60 * 1000L;
        final SseEmitter sseEmitter = new SseEmitter(timeout);
        sseService.register(teamBottariId, sseEmitter);

        return sseEmitter;
    }

    @GetMapping("test/test")
    public void test() {
        sseService.sendByTeamBottariId(1L,
                new SseMessage(
                        SseResourceType.ASSIGNED_ITEM,
                        SseEventType.CHANGE,
                        "test message"
                ));
    }
}
