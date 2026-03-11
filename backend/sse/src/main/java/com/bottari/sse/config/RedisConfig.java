package com.bottari.sse.config;

import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisTaskExecutorProperties redisTaskExecutorProperties;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            final RedisConnectionFactory redisConnectionFactory,
            final Executor redisTaskExecutor
    ) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.setTaskExecutor(redisTaskExecutor);

        return container;
    }

    @Bean
    public Executor redisTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 기본 스레드 수 (항상 유지되는 스레드)
        executor.setCorePoolSize(redisTaskExecutorProperties.getCorePoolSize());

        // 최대 스레드 수 (부하가 높을 때 증가)
        executor.setMaxPoolSize(redisTaskExecutorProperties.getMaxPoolSize());

        // 큐 용량 (스레드가 모두 사용 중일 때 대기)
        executor.setQueueCapacity(redisTaskExecutorProperties.getQueueCapacity());

        // 스레드 이름 prefix
        executor.setThreadNamePrefix(redisTaskExecutorProperties.getThreadNamePrefix());

        // 종료 대기 시간
        executor.setWaitForTasksToCompleteOnShutdown(redisTaskExecutorProperties.isWaitForTasksToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds(redisTaskExecutorProperties.getAwaitTerminationSeconds());

        executor.setTaskDecorator(new ObservabilityTaskDecorator());

        executor.initialize();

        return executor;
    }
}
