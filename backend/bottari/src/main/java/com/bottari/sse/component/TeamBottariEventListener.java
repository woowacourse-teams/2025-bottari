package com.bottari.sse.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamBottariEventListener {

    private final SseService sseService;
}
