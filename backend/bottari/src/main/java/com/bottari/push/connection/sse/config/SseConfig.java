package com.bottari.push.connection.sse.config;

import com.bottari.push.connection.sse.SseChannel;
import com.bottari.push.connection.sse.external.RedisSseChannel;
import com.bottari.push.connection.sse.inmemory.InMemorySseChannel;
import com.bottari.push.connection.sse.inmemory.SseSessions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;

/*
 * SSE Channel 설정 정책
 *
 * test1, test3  → InMemory SSE
 * test2, test4  → Redis SSE
 * 그 외 전체    → Redis SSE (기본 정책)
 *
 * 향후 운영 환경은 Redis SSE로 일원화 예정
 */
@Slf4j
@Configuration
public class SseConfig {

    @Profile("test1 | test3")
    @Bean
    public SseChannel inMemorySseChannel(final SseSessions sseSessions) {
        log.info("[SSE] mode = IN_MEMORY");

        return new InMemorySseChannel(sseSessions);
    }

    @Profile("!(test1 | test3)")
    @Bean
    public SseChannel redisSseChannel(final RedisTemplate<String, Object> redisTemplate) {
        log.info("[SSE] mode = REDIS");

        return new RedisSseChannel(redisTemplate);
    }
}
