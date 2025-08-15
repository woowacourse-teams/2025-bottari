package com.bottari.fcm;

import com.bottari.fcm.dto.MessageType;
import com.bottari.fcm.dto.SendMessageRequest;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class FcmMessageConverter {

    private static final String TEAM_BOTTARI_ID = "teamBottariId";
    private static final String TEAM_BOTTARI_TITLE = "teamBottariTitle";
    public static final String TEAM_ITEM_NAME = "teamItemName";

    public SendMessageRequest convert(
            final TeamBottari teamBottari,
            final TeamSharedItemInfo info,
            final MessageType messageType
    ) {
        final Long teamBottariId = teamBottari.getId();
        final String teamBottariTitle = teamBottari.getTitle();
        final String itemName = info.getName();

        return new SendMessageRequest(
                Map.of(
                        TEAM_BOTTARI_ID, String.valueOf(teamBottariId),
                        TEAM_BOTTARI_TITLE, String.valueOf(teamBottariTitle),
                        TEAM_ITEM_NAME, String.valueOf(itemName)
                ),
                messageType
        );
    }

    public SendMessageRequest convert(
            final TeamBottari teamBottari,
            final TeamAssignedItemInfo info,
            final MessageType messageType
    ) {
        final Long teamBottariId = teamBottari.getId();
        final String teamBottariTitle = teamBottari.getTitle();
        final String itemName = info.getName();

        return new SendMessageRequest(
                Map.of(
                        TEAM_BOTTARI_ID, String.valueOf(teamBottariId),
                        TEAM_BOTTARI_TITLE, String.valueOf(teamBottariTitle),
                        TEAM_ITEM_NAME, String.valueOf(itemName)
                ),
                messageType
        );
    }
}
