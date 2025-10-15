package com.bottari.teambottari.adapter;

import com.bottari.push.ChannelType;
import com.bottari.push.PushManager;
import com.bottari.push.message.MessageEventType;
import com.bottari.push.message.MessageResourceType;
import com.bottari.push.message.PushMessage;
import com.bottari.teambottari.adapter.data.ChangeAssignedItemData;
import com.bottari.teambottari.adapter.data.CheckTeamItemData;
import com.bottari.teambottari.adapter.data.CreateAssignedItemData;
import com.bottari.teambottari.adapter.data.CreateAssignedItemInfoData;
import com.bottari.teambottari.adapter.data.CreateTeamMemberData;
import com.bottari.teambottari.adapter.data.CreateTeamSharedItemData;
import com.bottari.teambottari.adapter.data.CreateTeamSharedItemInfoData;
import com.bottari.teambottari.adapter.data.DeleteAssignedItemData;
import com.bottari.teambottari.adapter.data.DeleteAssignedItemInfoData;
import com.bottari.teambottari.adapter.data.DeleteTeamSharedItemData;
import com.bottari.teambottari.adapter.data.DeleteTeamSharedItemInfoData;
import com.bottari.teambottari.adapter.data.ExitTeamMemberData;
import com.bottari.teambottari.dto.ReadAssignedItemResponse;
import com.bottari.teambottari.dto.ReadSharedItemResponse;
import com.bottari.teambottari.event.ChangeTeamAssignedItemEvent;
import com.bottari.teambottari.event.CheckTeamAssignedItemEvent;
import com.bottari.teambottari.event.CheckTeamSharedItemEvent;
import com.bottari.teambottari.event.CreateAssignedItemEvent;
import com.bottari.teambottari.event.CreateTeamMemberEvent;
import com.bottari.teambottari.event.CreateTeamSharedItemEvent;
import com.bottari.teambottari.event.DeleteAssignedItemEvent;
import com.bottari.teambottari.event.DeleteTeamSharedItemEvent;
import com.bottari.teambottari.event.ExitTeamMemberEvent;
import com.bottari.teambottari.service.TeamAssignedItemService;
import com.bottari.teambottari.service.TeamMemberService;
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

    private final PushManager pushManager;
    private final TeamMemberService teamMemberService;
    private final TeamSharedItemService teamSharedItemService;
    private final TeamAssignedItemService teamAssignedItemService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateTeamMemberEvent(final CreateTeamMemberEvent event) {
        final PushMessage pushMessage = new PushMessage(
                MessageResourceType.TEAM_MEMBER,
                MessageEventType.CREATE,
                CreateTeamMemberData.from(event)
        );
        final List<Long> memberIds = teamMemberService.getMemberIdsByTeamBottariId(event.getTeamBottariId());
        pushManager.message(pushMessage)
                .to(memberIds)
                .unicast()
                .viaConnection(ChannelType.SSE)
                .send();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateTeamSharedItemEvent(final CreateTeamSharedItemEvent event) {
        final List<ReadSharedItemResponse> idempotentInfos =
                teamSharedItemService.getAllByTeamBottariId(event.getTeamBottariId());
        final PushMessage createSharedItemInfoMessage = new PushMessage(
                MessageResourceType.SHARED_ITEM_INFO,
                MessageEventType.CREATE,
                CreateTeamSharedItemInfoData.of(idempotentInfos, event)
        );
        final PushMessage createSharedItemMessage = new PushMessage(
                MessageResourceType.SHARED_ITEM,
                MessageEventType.CREATE,
                CreateTeamSharedItemData.from(event)
        );
        final List<Long> memberIds = teamMemberService.getMemberIdsByTeamBottariId(event.getTeamBottariId());
        pushManager.message(createSharedItemInfoMessage)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .send();
        pushManager.message(createSharedItemMessage)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .send();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeleteTeamSharedItemEvent(final DeleteTeamSharedItemEvent event) {
        final List<ReadSharedItemResponse> idempotentInfos =
                teamSharedItemService.getAllByTeamBottariId(event.getTeamBottariId());
        final PushMessage deleteSharedItemInfoMessage = new PushMessage(
                MessageResourceType.SHARED_ITEM_INFO,
                MessageEventType.DELETE,
                DeleteTeamSharedItemInfoData.of(idempotentInfos, event)
        );
        final PushMessage deleteSharedItemMessage = new PushMessage(
                MessageResourceType.SHARED_ITEM,
                MessageEventType.DELETE,
                DeleteTeamSharedItemData.from(event)
        );
        final List<Long> memberIds = teamMemberService.getMemberIdsByTeamBottariId(event.getTeamBottariId());
        pushManager.message(deleteSharedItemInfoMessage)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .send();
        pushManager.message(deleteSharedItemMessage)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .send();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCheckTeamSharedItemEvent(final CheckTeamSharedItemEvent event) {
        final PushMessage message = new PushMessage(
                MessageResourceType.SHARED_ITEM,
                MessageEventType.CHECK,
                CheckTeamItemData.from(event)
        );
        final List<Long> memberIds = teamMemberService.getMemberIdsByTeamBottariId(event.getTeamBottariId());
        pushManager.message(message)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .send();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCheckTeamAssignedItemEvent(final CheckTeamAssignedItemEvent event) {
        final PushMessage message = new PushMessage(
                MessageResourceType.ASSIGNED_ITEM,
                MessageEventType.CHECK,
                CheckTeamItemData.from(event)
        );
        final List<Long> memberIds = teamMemberService.getMemberIdsByTeamBottariId(event.getTeamBottariId());
        pushManager.message(message)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .send();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateAssignedItemEvent(final CreateAssignedItemEvent event) {
        final List<ReadAssignedItemResponse> idempotentInfos =
                teamAssignedItemService.getAllByTeamBottariId(event.getTeamBottariId());
        final PushMessage createAssignedItemInfoMessage = new PushMessage(
                MessageResourceType.ASSIGNED_ITEM_INFO,
                MessageEventType.CREATE,
                CreateAssignedItemInfoData.of(idempotentInfos, event)
        );
        final PushMessage createAssignedItemMessage = new PushMessage(
                MessageResourceType.ASSIGNED_ITEM,
                MessageEventType.CREATE,
                CreateAssignedItemData.from(event)
        );
        final List<Long> memberIds = teamMemberService.getMemberIdsByTeamBottariId(event.getTeamBottariId());
        pushManager.message(createAssignedItemInfoMessage)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .send();
        pushManager.message(createAssignedItemMessage)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .send();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChangeAssignedItemEvent(final ChangeTeamAssignedItemEvent event) {
        final PushMessage message = new PushMessage(
                MessageResourceType.ASSIGNED_ITEM_INFO,
                MessageEventType.CHANGE,
                ChangeAssignedItemData.from(event)
        );
        final List<Long> memberIds = teamMemberService.getMemberIdsByTeamBottariId(event.getTeamBottariId());
        pushManager.message(message)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .send();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeleteAssignedItemEvent(final DeleteAssignedItemEvent event) {
        final List<ReadAssignedItemResponse> idempotentInfos = teamAssignedItemService.getAllByTeamBottariId(
                event.getTeamBottariId());
        final PushMessage deleteAssignedItemInfoMessage = new PushMessage(
                MessageResourceType.ASSIGNED_ITEM_INFO,
                MessageEventType.DELETE,
                DeleteAssignedItemInfoData.of(idempotentInfos, event)
        );
        final PushMessage deleteAssignedItemMessage = new PushMessage(
                MessageResourceType.ASSIGNED_ITEM,
                MessageEventType.DELETE,
                DeleteAssignedItemData.from(event)
        );
        final List<Long> memberIds = teamMemberService.getMemberIdsByTeamBottariId(event.getTeamBottariId());
        pushManager.message(deleteAssignedItemInfoMessage)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .send();
        pushManager.message(deleteAssignedItemMessage)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .send();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleExitTeamMemberEvent(final ExitTeamMemberEvent event) {
        final PushMessage message = new PushMessage(
                MessageResourceType.TEAM_MEMBER,
                MessageEventType.DELETE,
                ExitTeamMemberData.from(event)
        );
        final List<Long> memberIds = teamMemberService.getMemberIdsByTeamBottariId(event.getTeamBottariId());
        pushManager.message(message)
                .to(memberIds)
                .multicast()
                .viaConnection(ChannelType.SSE)
                .viaNotification()
                .send();
    }
}
