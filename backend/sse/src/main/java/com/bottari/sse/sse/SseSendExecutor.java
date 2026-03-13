package com.bottari.sse.sse;

import com.bottari.sse.config.ObservabilityTaskDecorator;
import com.bottari.sse.config.RedisTaskExecutorProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class SseSendExecutor extends ThreadPoolTaskExecutor {

    private final RedisTaskExecutorProperties redisTaskExecutorProperties;

    public SseSendExecutor(final RedisTaskExecutorProperties properties) {
        this.redisTaskExecutorProperties = properties;
        this.setCorePoolSize(redisTaskExecutorProperties.getCorePoolSize());
        this.setMaxPoolSize(redisTaskExecutorProperties.getMaxPoolSize());
        this.setQueueCapacity(redisTaskExecutorProperties.getQueueCapacity());
        this.setThreadNamePrefix(redisTaskExecutorProperties.getThreadNamePrefix());
        this.setWaitForTasksToCompleteOnShutdown(redisTaskExecutorProperties.isWaitForTasksToCompleteOnShutdown());
        this.setAwaitTerminationSeconds(redisTaskExecutorProperties.getAwaitTerminationSeconds());
        this.setTaskDecorator(new ObservabilityTaskDecorator());
        this.initialize();
    }
}
