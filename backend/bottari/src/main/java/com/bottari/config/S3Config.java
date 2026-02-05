package com.bottari.config;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Bean
    @Profile("default")
    public S3Client localS3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .httpClientBuilder(ApacheHttpClient.builder()
                        .maxConnections(10)
                        .connectionAcquisitionTimeout(Duration.ofSeconds(1))
                        .connectionTimeout(Duration.ofSeconds(2))
                        .socketTimeout(Duration.ofSeconds(90))
                        .connectionMaxIdleTime(Duration.ofSeconds(30))
                        .connectionTimeToLive(Duration.ofMinutes(2))
                )
                .overrideConfiguration(ClientOverrideConfiguration.builder()
                        .apiCallAttemptTimeout(Duration.ofSeconds(60))
                        .apiCallTimeout(Duration.ofMinutes(3))
                        .build()
                )
                .build();
    }

    @Bean
    @Profile("dev | prod")
    public S3Client devProdS3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .httpClientBuilder(ApacheHttpClient.builder()
                        .maxConnections(10)
                        .connectionAcquisitionTimeout(Duration.ofSeconds(1))
                        .connectionTimeout(Duration.ofSeconds(2))
                        .socketTimeout(Duration.ofSeconds(90))
                        .connectionMaxIdleTime(Duration.ofSeconds(30))
                        .connectionTimeToLive(Duration.ofMinutes(2))
                )
                .overrideConfiguration(ClientOverrideConfiguration.builder()
                        .apiCallAttemptTimeout(Duration.ofSeconds(60))
                        .apiCallTimeout(Duration.ofMinutes(3))
                        .build()
                )
                .build();
    }
}
