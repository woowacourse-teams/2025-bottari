package com.bottari.sse.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "sseTaskExecutor")
    public Executor sseTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 기본 스레드 수 (항상 유지되는 스레드)
        executor.setCorePoolSize(10);

        // 최대 스레드 수 (부하가 높을 때 증가)
        executor.setMaxPoolSize(50);

        // 큐 용량 (스레드가 모두 사용 중일 때 대기)
        executor.setQueueCapacity(100);

        // 스레드 이름 prefix
        executor.setThreadNamePrefix("sse-async-");

        // 종료 대기 시간
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        executor.setTaskDecorator(new ObservabilityTaskDecorator());

        executor.initialize();
        return executor;
    }
}
