package com.bottari.log;

import com.bottari.config.datasource.RoutingDataSource;
import com.bottari.push.connection.sse.SseChannel;
import com.bottari.push.connection.sse.external.RedisSseChannel;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationSettingLogger implements ApplicationRunner {

    private final DataSource dataSource;
    private final SseChannel sseChannel;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n==================== APPLICATION SETTING ====================\n");
        appendDataSourceSetting(sb);
        appendSseChannelSetting(sb);
        sb.append("\n=============================================================\n\n");

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
}
