package com.bottari.teambottari.event;

public record TeamSharedItemCheckEvent(
        Long teamBottariId,
        Long memberId,
        Long infoId,
        Long itemId,
        boolean isChecked
) {
}
