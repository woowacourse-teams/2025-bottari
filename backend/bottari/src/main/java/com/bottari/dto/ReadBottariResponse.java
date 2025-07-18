package com.bottari.dto;

import com.bottari.domain.Alarm;
import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import java.util.List;

public record ReadBottariResponse(
        Long id,
        String title,
        List<BottariItemResponse> items,
        AlarmResponse alarm
) {

    public static ReadBottariResponse of(
            final Bottari bottari,
            final List<BottariItem> bottariItems,
            final Alarm alarm
    ) {
        return new ReadBottariResponse(
                bottari.getId(),
                bottari.getTitle(),
                bottariItems.stream().map(BottariItemResponse::from).toList(),
                AlarmResponse.from(alarm)
        );
    }

    public record BottariItemResponse(
            Long id,
            String name
    ) {

        public static BottariItemResponse from(final BottariItem bottariItem) {
            return new BottariItemResponse(
                    bottariItem.getId(),
                    bottariItem.getName()
            );
        }
    }
}
