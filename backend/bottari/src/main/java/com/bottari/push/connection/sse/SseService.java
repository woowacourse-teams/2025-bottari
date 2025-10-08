package com.bottari.push.connection.sse;

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
        sseEmitter.onCompletion(() -> sseSessions.remove(memberId));
        sseEmitter.onTimeout(() -> sseSessions.remove(memberId));
        sseEmitter.onError(throwable -> sseSessions.remove(memberId));
        sseSessions.save(memberId, sseEmitter);
    }
}
