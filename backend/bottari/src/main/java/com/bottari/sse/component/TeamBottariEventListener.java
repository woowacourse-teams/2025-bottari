package com.bottari.sse.component;

import com.bottari.sse.dto.CreateAssignedItemData;
import com.bottari.sse.message.SseEventType;
import com.bottari.sse.message.SseMessage;
import com.bottari.sse.message.SseResourceType;
import com.bottari.teambottari.event.CreateAssignedItemEvent;
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
    public void handleCreateAssignedItemEvent(final CreateAssignedItemEvent event) {
        final SseMessage message = new SseMessage(
                SseResourceType.ASSIGNED_ITEM,
                SseEventType.CREATE,
                CreateAssignedItemData.from(event)
        );
        sseService.sendByTeamBottariId(event.teamBottariId(), message);
    }
}
