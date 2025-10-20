package com.bottari.push.connection.sse.config;

import com.bottari.push.connection.sse.SseChannel;
import com.bottari.push.connection.sse.external.RedisSseChannel;
import com.bottari.push.connection.sse.inmemory.InMemorySseChannel;
import com.bottari.push.connection.sse.inmemory.SseSessions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;


/*
 * 부하 테스트 이후, 삭제될 설정 파일
 *
 * dev1 - 단일 DB InMemory SSE 테스트 환경
 * dev2 - Read/Write 분리 InMemory SSE 테스트 환경
 *
 * 1. InMemory SSE   {"dev1", "dev2"}   -> 부하테스트를 위해서 하위 호환되도록
 * 2. Redis SSE      {"!dev1", "!dev2"}   -> 부하테스트 이후에는 Redis로 통합
 * */
@Configuration
public class SseConfig {

    @Profile("!(dev1 | dev2)")
    @Bean
    public SseChannel redisSseChannel(final RedisTemplate<String, Object> redisTemplate) {
        return new RedisSseChannel(redisTemplate);
    }

    @Profile("dev1 | dev2")
    @Bean
    public SseChannel inMemorySseChannel(final SseSessions sseSessions) {
        return new InMemorySseChannel(sseSessions);
    }
}
