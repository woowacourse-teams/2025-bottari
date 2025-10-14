package com.bottari.teambottari.adapter;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.push.message.MessageEventType;
import com.bottari.push.message.MessageResourceType;
import com.bottari.push.message.PushMessage;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TeamBottariMessageConverter {

    private static final String TEAM_BOTTARI_ID = "teamBottariId";
    private static final String TEAM_BOTTARI_TITLE = "teamBottariTitle";
    private static final String TEAM_ITEM_NAME = "teamItemName";
    private static final String TEAM_SHARED_ITEM_NAMES = "teamSharedItemNames";
    private static final String TEAM_ASSIGNED_ITEM_NAMES = "teamAssignedItemNames";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PushMessage convert(
            final MessageResourceType resourceType,
            final MessageEventType eventType,
            final TeamBottari teamBottari,
            final TeamSharedItemInfo info
    ) {
        final Long teamBottariId = teamBottari.getId();
        final String teamBottariTitle = teamBottari.getTitle();
        final String itemName = info.getName();

        return new PushMessage(
                resourceType,
                eventType,
                Map.of(
                        TEAM_BOTTARI_ID, String.valueOf(teamBottariId),
                        TEAM_BOTTARI_TITLE, String.valueOf(teamBottariTitle),
                        TEAM_ITEM_NAME, String.valueOf(itemName)
                )
        );
    }

    public PushMessage convert(
            final MessageResourceType resourceType,
            final MessageEventType eventType,
            final TeamBottari teamBottari,
            final TeamAssignedItemInfo info
    ) {
        final Long teamBottariId = teamBottari.getId();
        final String teamBottariTitle = teamBottari.getTitle();
        final String itemName = info.getName();

        return new PushMessage(
                resourceType,
                eventType,
                Map.of(
                        TEAM_BOTTARI_ID, String.valueOf(teamBottariId),
                        TEAM_BOTTARI_TITLE, String.valueOf(teamBottariTitle),
                        TEAM_ITEM_NAME, String.valueOf(itemName)
                )
        );
    }

    public PushMessage convert(
            final MessageResourceType resourceType,
            final MessageEventType eventType,
            final TeamBottari teamBottari,
            final List<TeamSharedItemInfo> uncheckedSharedItemInfos,
            final List<TeamAssignedItemInfo> uncheckedAssignedItemsInfos
    ) {
        final Long teamBottariId = teamBottari.getId();
        final String teamBottariTitle = teamBottari.getTitle();
        final List<String> sharedItemNames = uncheckedSharedItemInfos.stream()
                .map(TeamSharedItemInfo::getName)
                .toList();
        final List<String> assignedItemNames = uncheckedAssignedItemsInfos.stream()
                .map(TeamAssignedItemInfo::getName)
                .toList();

        try {
            return new PushMessage(
                    resourceType,
                    eventType,
                    Map.of(
                            TEAM_BOTTARI_ID, String.valueOf(teamBottariId),
                            TEAM_BOTTARI_TITLE, String.valueOf(teamBottariTitle),
                            TEAM_SHARED_ITEM_NAMES, objectMapper.writeValueAsString(sharedItemNames),
                            TEAM_ASSIGNED_ITEM_NAMES, objectMapper.writeValueAsString(assignedItemNames)
                    )
            );
        } catch (final JsonProcessingException e) {
            throw new BusinessException(ErrorCode.FCM_MESSAGE_CONVERT_FAIL);
        }
    }
}
