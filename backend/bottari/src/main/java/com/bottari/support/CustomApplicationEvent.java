package com.bottari.support;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CustomApplicationEvent {

    private final LocalDateTime publishedAt;

    public CustomApplicationEvent() {
        this.publishedAt = LocalDateTime.now();
    }
}
