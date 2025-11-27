package com.bottari.push.connection.sse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("test1 | test3")
@Configuration
@EnableScheduling
public class SchedulerConfig {
}
