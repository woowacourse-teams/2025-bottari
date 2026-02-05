package com.bottari.sse.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sse.async")
@Getter
@Setter
public class RedisTaskExecutorProperties {

    /**
     * 기본 스레드 수 (항상 유지되는 스레드)
     */
    private int corePoolSize = 10;

    /**
     * 최대 스레드 수 (부하가 높을 때 증가)
     */
    private int maxPoolSize = 50;

    /**
     * 큐 용량 (스레드가 모두 사용 중일 때 대기)
     */
    private int queueCapacity = 100;

    /**
     * 스레드 이름 prefix
     */
    private String threadNamePrefix = "sse-async-";

    /**
     * 종료 시 작업 완료 대기 여부
     */
    private boolean waitForTasksToCompleteOnShutdown = true;

    /**
     * 종료 대기 시간 (초)
     */
    private int awaitTerminationSeconds = 30;
}
