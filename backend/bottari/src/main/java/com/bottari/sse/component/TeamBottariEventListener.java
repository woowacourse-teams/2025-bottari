package com.bottari.sse.component;

import com.bottari.sse.dto.ChangeAssignedItemData;
import com.bottari.sse.dto.CheckTeamItemData;
import com.bottari.sse.dto.CreateAssignedItemData;
import com.bottari.sse.dto.CreateTeamMemberData;
import com.bottari.sse.dto.CreateTeamSharedItemData;
import com.bottari.sse.dto.CreateTeamSharedItemInfoData;
import com.bottari.sse.dto.DeleteAssignedItemData;
import com.bottari.sse.dto.DeleteTeamSharedItemData;
import com.bottari.sse.dto.DeleteTeamSharedItemInfoData;
import com.bottari.sse.dto.ExitTeamMemberData;
import com.bottari.sse.message.SseEventType;
import com.bottari.sse.message.SseMessage;
import com.bottari.sse.message.SseResourceType;
import com.bottari.teambottari.dto.ReadSharedItemResponse;
import com.bottari.teambottari.event.ChangeTeamAssignedItemEvent;
import com.bottari.teambottari.event.CheckTeamAssignedItemEvent;
import com.bottari.teambottari.event.CheckTeamSharedItemEvent;
import com.bottari.teambottari.event.CreateAssignedItemEvent;
import com.bottari.teambottari.event.CreateTeamSharedItemEvent;
import com.bottari.teambottari.event.DeleteAssignedItemEvent;
import com.bottari.teambottari.event.DeleteTeamSharedItemEvent;
import com.bottari.teambottari.event.ExitTeamMemberEvent;
import com.bottari.teambottari.service.CreateTeamMemberEvent;
import com.bottari.teambottari.service.TeamSharedItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TeamBottariEventListener {

    private final SseService sseService;
    private final TeamSharedItemService teamSharedItemService;

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

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateTeamSharedItemEvent(final CreateTeamSharedItemEvent event) {
        final List<ReadSharedItemResponse> infos =
                teamSharedItemService.getAllByTeamBottariId(event.getTeamBottariId());
        final SseMessage createSharedItemInfoMessage = new SseMessage(
                SseResourceType.SHARED_ITEM_INFO,
                SseEventType.CREATE,
                CreateTeamSharedItemInfoData.of(infos, event)
        );
        final SseMessage createSharedItemMessage = new SseMessage(
                SseResourceType.SHARED_ITEM,
                SseEventType.CREATE,
                CreateTeamSharedItemData.from(event)
        );
        sseService.sendByTeamBottariId(event.getTeamBottariId(), createSharedItemInfoMessage);
        sseService.sendByTeamBottariId(event.getTeamBottariId(), createSharedItemMessage);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeleteTeamSharedItemEvent(final DeleteTeamSharedItemEvent event) {
        final List<ReadSharedItemResponse> infos =
                teamSharedItemService.getAllByTeamBottariId(event.getTeamBottariId());
        final SseMessage deleteSharedItemInfoMessage = new SseMessage(
                SseResourceType.SHARED_ITEM_INFO,
                SseEventType.DELETE,
                DeleteTeamSharedItemInfoData.of(infos, event)
        );
        final SseMessage deleteSharedItemMessage = new SseMessage(
                SseResourceType.SHARED_ITEM,
                SseEventType.DELETE,
                DeleteTeamSharedItemData.from(event)
        );
        sseService.sendByTeamBottariId(event.getTeamBottariId(), deleteSharedItemInfoMessage);
        sseService.sendByTeamBottariId(event.getTeamBottariId(), deleteSharedItemMessage);
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

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateAssignedItemEvent(final CreateAssignedItemEvent event) {
        final SseMessage message = new SseMessage(
                SseResourceType.ASSIGNED_ITEM_INFO,
                SseEventType.CREATE,
                CreateAssignedItemData.from(event)
        );
        sseService.sendByTeamBottariId(event.getTeamBottariId(), message);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChangeAssignedItemEvent(final ChangeTeamAssignedItemEvent event) {
        final SseMessage message = new SseMessage(
                SseResourceType.ASSIGNED_ITEM_INFO,
                SseEventType.CHANGE,
                ChangeAssignedItemData.from(event)
        );
        sseService.sendByTeamBottariId(event.getTeamBottariId(), message);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeleteAssignedItemEvent(final DeleteAssignedItemEvent event) {
        final SseMessage message = new SseMessage(
                SseResourceType.ASSIGNED_ITEM_INFO,
                SseEventType.DELETE,
                DeleteAssignedItemData.from(event)
        );
        sseService.sendByTeamBottariId(event.getTeamBottariId(), message);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleExitTeamMemberEvent(final ExitTeamMemberEvent event) {
        final SseMessage message = new SseMessage(
                SseResourceType.TEAM_MEMBER,
                SseEventType.DELETE,
                ExitTeamMemberData.from(event)
        );
        sseService.sendByTeamBottariId(event.getTeamBottariId(), message);
    }
}
