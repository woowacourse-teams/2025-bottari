package com.bottari.config.datasource;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile("default")
@Configuration
@EnableTransactionManagement
public class LocalDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties localDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource localDataSource(final DataSourceProperties localDataSourceProperties){
        return localDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Primary
    public DataSource dataSource(final DataSource localDataSource) {
        return new LazyConnectionDataSourceProxy(localDataSource);
    }
}
