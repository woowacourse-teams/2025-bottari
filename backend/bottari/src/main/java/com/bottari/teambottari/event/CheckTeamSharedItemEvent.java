package com.bottari.teambottari.event;

import com.bottari.support.CustomApplicationEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CheckTeamSharedItemEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final Long memberId;
    private final Long infoId;
    private final Long itemId;
    private final boolean isChecked;
}
