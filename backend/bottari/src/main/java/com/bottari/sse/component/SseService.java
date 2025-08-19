package com.bottari.sse.component;

import com.bottari.sse.dto.ConnectSuccessData;
import com.bottari.sse.message.SseMessage;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseRepository sseRepository;

    public void sendByTeamBottariId(
            final Long teamBottariId,
            final SseMessage message
    ) {
        final List<SseEmitter> sseEmitters = sseRepository.findByTeamBottariId(teamBottariId);
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

    public void register(
            final Long teamBottariId,
            final SseEmitter sseEmitter
    ) {
        sseEmitter.onCompletion(() -> sseRepository.remove(teamBottariId, sseEmitter));
        sseEmitter.onTimeout(() -> sseRepository.remove(teamBottariId, sseEmitter));
        sseEmitter.onError(throwable -> sseRepository.remove(teamBottariId, sseEmitter));
        sseRepository.save(teamBottariId, sseEmitter);
        try {
            sseEmitter.send(new ConnectSuccessData("Connect Success"), MediaType.APPLICATION_JSON);
        } catch (final Exception ignored) {
        }
    }
}
