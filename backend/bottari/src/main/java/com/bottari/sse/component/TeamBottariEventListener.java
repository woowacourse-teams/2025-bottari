package com.bottari.sse.component;

import com.bottari.sse.dto.CreateTeamSharedItemData;
import com.bottari.sse.dto.DeleteTeamSharedItemData;
import com.bottari.sse.message.SseEventType;
import com.bottari.sse.message.SseMessage;
import com.bottari.sse.message.SseResourceType;
import com.bottari.teambottari.event.CreateTeamSharedItemEvent;
import com.bottari.teambottari.event.DeleteTeamSharedItemEvent;
import com.bottari.sse.dto.CheckTeamItemData;
import com.bottari.sse.message.SseEventType;
import com.bottari.sse.message.SseMessage;
import com.bottari.sse.message.SseResourceType;
import com.bottari.teambottari.event.CheckTeamAssignedItemEvent;
import com.bottari.teambottari.event.CheckTeamSharedItemEvent;
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
    public void handleCreateTeamSharedItemEvent(final CreateTeamSharedItemEvent event) {
        final SseMessage message = new SseMessage(
                SseResourceType.SHARED_ITEM_INFO,
                SseEventType.CHANGE,
                CreateTeamSharedItemData.from(event)
        );
        sseService.sendByTeamBottariId(event.getTeamBottariId(), message);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeleteTeamSharedItemEvent(final DeleteTeamSharedItemEvent event) {
        final SseMessage message = new SseMessage(
                SseResourceType.SHARED_ITEM_INFO,
                SseEventType.CHANGE,
                DeleteTeamSharedItemData.from(event)
        );
        sseService.sendByTeamBottariId(event.getTeamBottariId(), message);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCheckTeamSharedItemEvent(final CheckTeamSharedItemEvent event) {
        final SseMessage message = new SseMessage(
                SseResourceType.SHARED_ITEM,
                SseEventType.CHANGE,
                CheckTeamItemData.from(event)
        );
        sseService.sendByTeamBottariId(event.getTeamBottariId(), message);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCheckTeamAssignedItemEvent(final CheckTeamAssignedItemEvent event) {
        final SseMessage message = new SseMessage(
                SseResourceType.ASSIGNED_ITEM,
                SseEventType.CHANGE,
                CheckTeamItemData.from(event)
        );
        sseService.sendByTeamBottariId(event.getTeamBottariId(), message);
    }
}
