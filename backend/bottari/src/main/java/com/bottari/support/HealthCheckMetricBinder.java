package com.bottari.support;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthCheckMetricBinder implements MeterBinder {

    private final HealthEndpoint healthEndpoint;

    @Override
    public void bindTo(final MeterRegistry meterRegistry) {
        Gauge.builder(
                "app.health.status",
                () -> {
                    final HealthComponent health = healthEndpoint.health();
                    final Status status = health.getStatus();
                    if (Status.UP.equals(status)) {
                        return 1;
                    }
                    return 0;
                }
        ).register(meterRegistry);
    }
}
