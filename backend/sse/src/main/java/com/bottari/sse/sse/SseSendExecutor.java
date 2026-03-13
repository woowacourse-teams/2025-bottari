package com.bottari.sse.sse;

import com.bottari.sse.config.ObservabilityTaskDecorator;
import com.bottari.sse.config.RedisTaskExecutorProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseSendExecutor extends ThreadPoolTaskExecutor {

    private final RedisTaskExecutorProperties redisTaskExecutorProperties;
    private final SseRefreshRegistry sseRefreshRegistry;

    @PostConstruct
    public void init() {
        this.setCorePoolSize(redisTaskExecutorProperties.getCorePoolSize());
        this.setMaxPoolSize(redisTaskExecutorProperties.getMaxPoolSize());
        this.setQueueCapacity(redisTaskExecutorProperties.getQueueCapacity());
        this.setThreadNamePrefix(redisTaskExecutorProperties.getThreadNamePrefix());
        this.setWaitForTasksToCompleteOnShutdown(redisTaskExecutorProperties.isWaitForTasksToCompleteOnShutdown());
        this.setAwaitTerminationSeconds(redisTaskExecutorProperties.getAwaitTerminationSeconds());
        this.setRejectedExecutionHandler(new SseExecutorRejectedHandler(sseRefreshRegistry));
        this.setTaskDecorator(new ObservabilityTaskDecorator());
        this.initialize();
    }

    public void submitSendTask(final SseSendTask task) {
        this.submit(task);
    }
}
