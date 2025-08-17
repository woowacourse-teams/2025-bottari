package com.bottari.sse.component;

import com.bottari.sse.message.SseEventType;
import com.bottari.sse.message.SseMessage;
import com.bottari.sse.message.SseResourceType;
import com.bottari.teambottari.service.CreateTeamMemberEvent;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TeamBottariEventListener {

    private final SseService sseService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listenTeamMemberEvent(final CreateTeamMemberEvent event) {
        final SseMessage sseMessage = new SseMessage(
                SseResourceType.TEAM_MEMBER,
                SseEventType.CREATE,
                Map.of(
                        "memberId", event.memberId(),
                        "name", event.name(),
                        "isOwner", event.isOwner()
                )
        );
        sseService.sendByTeamBottariId(event.teamBottariId(), sseMessage);
    }
}
