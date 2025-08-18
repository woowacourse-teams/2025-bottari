package com.bottari.sse.component;

import com.bottari.sse.dto.CreateTeamMemberData;
import com.bottari.sse.message.SseEventType;
import com.bottari.sse.message.SseMessage;
import com.bottari.sse.message.SseResourceType;
import com.bottari.teambottari.service.CreateTeamMemberEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TeamBottariEventListener {

    private final SseService sseService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateTeamMemberEvent(final CreateTeamMemberEvent event) {
        final SseMessage sseMessage = new SseMessage(
                SseResourceType.TEAM_MEMBER,
                SseEventType.CREATE,
                CreateTeamMemberData.from(event)
        );
        sseService.sendByTeamBottariId(event.getTeamBottariId(), sseMessage);
    }
}
