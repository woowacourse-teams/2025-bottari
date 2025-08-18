package com.bottari.support;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public abstract class CustomApplicationEvent {

    private final LocalDateTime publishedAt;

    public CustomApplicationEvent() {
        this.publishedAt = LocalDateTime.now();
    }
}
