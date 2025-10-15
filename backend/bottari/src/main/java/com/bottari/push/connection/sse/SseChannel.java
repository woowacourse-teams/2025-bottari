package com.bottari.push.connection.sse;

import com.bottari.push.ChannelType;
import com.bottari.push.connection.ConnectionChannel;
import com.bottari.push.message.PushMessage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public final class SseChannel implements ConnectionChannel {

    private final SseSessions sseSessions;

    @Override
    public void unicast(
            final PushMessage message,
            final Long memberId
    ) {
        final Optional<SseEmitter> sseEmitterOptional = sseSessions.findByMemberId(memberId);
        if (sseEmitterOptional.isEmpty()) {
            return;
        }
        try {
            sseEmitterOptional.get().send(message, MediaType.APPLICATION_JSON);
        } catch (final IOException ignore) {
                /*
                 클라이언트와의 연결이 끊기거나 타임아웃된 Emitter.send()를 시도할 때 IOException 발생 가능성 있음
                 register() 시점에 등록된 onCompletion, onTimeout, onError 콜백에서
                 이미 저장소(sseRepository)에서 제거되었거나 곧 제거될 예정이므로,
                 해당 예외는 자연스러운 상황으로 간주하고 무시(ignore)함.
                 */
        }
    }

    @Override
    public void multicast(
            final PushMessage message,
            final List<Long> memberIds
    ) {
        final List<SseEmitter> sseEmitters = sseSessions.findAllByMemberIds(memberIds);
        for (final SseEmitter sseEmitter : sseEmitters) {
            try {
                sseEmitter.send(message, MediaType.APPLICATION_JSON);
            } catch (final IOException ignore) {
                /*
                 클라이언트와의 연결이 끊기거나 타임아웃된 Emitter.send()를 시도할 때 IOException 발생 가능성 있음
                 register() 시점에 등록된 onCompletion, onTimeout, onError 콜백에서
                 이미 저장소(sseRepository)에서 제거되었거나 곧 제거될 예정이므로,
                 해당 예외는 자연스러운 상황으로 간주하고 무시(ignore)함.
                 */
            }
        }
    }

    @Override
    public void broadcast(final PushMessage message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ChannelType channelType() {
        return ChannelType.SSE;
    }
}
