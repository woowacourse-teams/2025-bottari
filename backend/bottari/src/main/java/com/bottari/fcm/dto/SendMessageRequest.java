package com.bottari.fcm.dto;

import com.bottari.teambottari.domain.TeamBottari;
import java.util.Map;
import java.util.stream.Collectors;

public record SendMessageRequest(
        Map<String, Object> data,
        MessageType messageType
) {

    private static final String TEAM_BOTTARI_ID = "teamBottariId";
    private static final String TEAM_BOTTARI_TITLE = "teamBottariTitle";

    public static SendMessageRequest of(
            final TeamBottari teamBottari,
            final MessageType messageType
    ) {
        return new SendMessageRequest(
                Map.of(
                        TEAM_BOTTARI_ID, teamBottari.getId(),
                        TEAM_BOTTARI_TITLE, teamBottari.getTitle()
                ),
                messageType
        );
    }

    public Map<String, String> dataToJsonValue() {
        return data.keySet()
                .stream()
                .collect(Collectors.toMap(key -> key, key -> data.get(key).toString()));
    }
}
