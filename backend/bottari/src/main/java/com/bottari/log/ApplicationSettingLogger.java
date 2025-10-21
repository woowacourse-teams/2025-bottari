package com.bottari.log;

import com.bottari.config.datasource.RoutingDataSource;
import com.bottari.push.connection.sse.SseChannel;
import com.bottari.push.connection.sse.external.RedisSseChannel;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import java.util.TreeMap;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationSettingLogger implements ApplicationRunner {

    private final DataSource dataSource;
    private final SseChannel sseChannel;
    private final ServerProperties serverProperties;
    private final ApplicationContext applicationContext;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n==================== APPLICATION SETTING ====================\n");
        appendDataSourceSetting(sb);
        appendSseChannelSetting(sb);
        appendTomcatSetting(sb);
        appendHikariSetting(sb);
        sb.append("=============================================================\n\n");

        log.info(sb.toString());
    }

    private void appendDataSourceSetting(final StringBuilder sb) {
        if (dataSource instanceof RoutingDataSource) {
            sb.append("[datasource] strategy = ROUTING");
        } else {
            sb.append("[datasource] strategy = SINGLE");
        }
        sb.append("\n");
    }

    private void appendSseChannelSetting(final StringBuilder sb) {
        if (sseChannel instanceof RedisSseChannel) {
            sb.append("[SSE] mode = REDIS");
        } else {
            sb.append("[SSE] mode = IN_MEMORY");
        }
        sb.append("\n");
    }

    private void appendTomcatSetting(final StringBuilder sb) {
        final Tomcat tomcat = serverProperties.getTomcat();
        sb.append("[tomcat]\n");
        sb.append("  threads.max = ").append(tomcat.getThreads().getMax()).append("\n");
        sb.append("  threads.min-spare = ").append(tomcat.getThreads().getMinSpare()).append("\n");
        sb.append("  max-connections = ").append(tomcat.getMaxConnections()).append("\n");
        sb.append("  accept-count = ").append(tomcat.getAcceptCount()).append("\n");
        sb.append("  connection-timeout = ").append(tomcat.getConnectionTimeout()).append("\n");
        sb.append("  keep-alive-timeout = ").append(tomcat.getKeepAliveTimeout()).append("\n");
    }

    private void appendHikariSetting(final StringBuilder sb) {
        final Map<String, HikariDataSource> pools = applicationContext.getBeansOfType(HikariDataSource.class);

        if (pools.isEmpty()) {
            sb.append("[hikari] no HikariDataSource beans found\n");
            return;
        }

        final boolean routing = (dataSource instanceof RoutingDataSource);
        sb.append("[hikari]\n");

        final Map<String, HikariDataSource> sorted = new TreeMap<>(pools);
        if (!routing) {
            // SINGLE: 첫 번째 하나만
            final Map.Entry<String, HikariDataSource> first = sorted.entrySet().iterator().next();
            appendOnePool(sb, first.getKey(), first.getValue());
            return;
        }

        // ROUTING: 앞의 2개만 (보통 master/replica)
        for (final Map.Entry<String, HikariDataSource> e : sorted.entrySet()) {
            appendOnePool(sb, e.getKey(), e.getValue());
        }
    }

    private void appendOnePool(
            final StringBuilder sb,
            final String beanName,
            final HikariDataSource ds
    ) {
        sb.append("  bean=").append(beanName)
                .append(", pool=").append(nullSafe(ds.getPoolName()))
                .append(", jdbcUrl=").append(nullSafe(ds.getJdbcUrl()))
                .append("\n");
        sb.append("    maximum-pool-size = ").append(ds.getMaximumPoolSize()).append("\n");
        sb.append("    minimum-idle      = ").append(ds.getMinimumIdle()).append("\n");
        sb.append("    connection-timeout= ").append(ds.getConnectionTimeout()).append("ms\n");
        sb.append("    max-lifetime      = ").append(ds.getMaxLifetime()).append("ms\n");
    }

    private String nullSafe(final String v) {
        return v == null ? "-" : v;
    }
}
