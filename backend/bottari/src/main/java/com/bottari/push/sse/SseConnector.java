package com.bottari.push.sse;

import com.bottari.config.MemberIdentifier;
import com.bottari.push.message.MessageEventType;
import com.bottari.push.message.MessageResourceType;
import com.bottari.push.message.PushMessage;
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

    /**
     * 멤버 ID 기반 SSE 연결
     */
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

    @GetMapping(path = "/team-bottaries/{teamBottariId}/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Override
    public SseEmitter connectTeamBottari(
            @PathVariable final Long teamBottariId
    ) {
        final long timeout = 60 * 60 * 1000L;
        final SseEmitter sseEmitter = new SseEmitter(timeout);
        sseService.registerByTeamBottariId(teamBottariId, sseEmitter);

        return sseEmitter;
    }

    @GetMapping("test/test")
    public void test() {
        sseService.sendByTeamBottariId(1L,
                new PushMessage(
                        MessageResourceType.ASSIGNED_ITEM,
                        MessageEventType.CHANGE,
                        "test message"
                ));
    }
}
