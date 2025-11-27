package com.bottari.push.connection.sse.inmemory;

import com.bottari.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseSessions sseSessions;
    private final MemberService memberService;

    public void register(
            final String ssaid,
            final SseEmitter sseEmitter
    ) {
        final Long memberId = memberService.getIdBySsaid(ssaid);
        sseEmitter.onCompletion(() -> cleanUpIfSame(memberId, sseEmitter));
        sseEmitter.onTimeout(() -> cleanUpIfSame(memberId, sseEmitter));
        sseEmitter.onError(t -> cleanUpIfSame(memberId, sseEmitter));

        sseSessions.save(memberId, sseEmitter);
    }

    private void cleanUpIfSame(
            final Long memberId,
            final SseEmitter targetEmitter
    ) {
        sseSessions.removeIfSame(memberId, targetEmitter);
    }
}
