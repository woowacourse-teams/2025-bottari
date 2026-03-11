package com.bottari.sse.config;

import io.opentelemetry.context.Context;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;

public class ObservabilityTaskDecorator implements TaskDecorator {

    @Override
    @NonNull
    public Runnable decorate(@NonNull final Runnable runnable) {
        return Context.current().wrap(runnable);
    }
}
