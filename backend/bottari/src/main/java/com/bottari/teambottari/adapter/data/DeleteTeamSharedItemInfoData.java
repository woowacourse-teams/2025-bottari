package com.bottari.teambottari.adapter.data;

import com.bottari.teambottari.dto.ReadSharedItemResponse;
import com.bottari.teambottari.event.DeleteTeamSharedItemEvent;
import java.time.LocalDateTime;
import java.util.List;

public record DeleteTeamSharedItemInfoData(
        Long teamBottariId,
        List<ReadSharedItemResponse> infos,
        LocalDateTime publishedAt
) {

    public static DeleteTeamSharedItemInfoData of(
            final List<ReadSharedItemResponse> infos,
            final DeleteTeamSharedItemEvent event
    ) {
        return new DeleteTeamSharedItemInfoData(
                event.getTeamBottariId(),
                infos,
                event.getPublishedAt()
        );
    }
}
