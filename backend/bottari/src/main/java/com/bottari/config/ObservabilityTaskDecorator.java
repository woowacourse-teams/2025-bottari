package com.bottari.config;


import io.micrometer.context.ContextRegistry;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.context.ContextSnapshotFactory;
import org.springframework.core.task.TaskDecorator;

public class ObservabilityTaskDecorator implements TaskDecorator {

    private static final ContextRegistry REGISTRY = ContextRegistry.getInstance();
    private static final ContextSnapshotFactory SNAPSHOT_FACTORY = ContextSnapshotFactory.builder()
            .contextRegistry(REGISTRY)
            .build();

    @Override
    public Runnable decorate(final Runnable runnable) {
        final ContextSnapshot snapshot = SNAPSHOT_FACTORY.captureAll();

        return () -> {
            try (final ContextSnapshot.Scope ignored = snapshot.setThreadLocals()) {
                runnable.run();
            }
        };
    }
}
