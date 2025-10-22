package com.bottari.sse.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationSettingLogger implements ApplicationRunner {

    private final ServerProperties serverProperties;
    private final Environment environment;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n==================== APPLICATION SETTING ====================\n");
        appendProfile(sb);
        appendTomcatSetting(sb);
        sb.append("=============================================================\n\n");

        log.info(sb.toString());
    }

    private void appendProfile(final StringBuilder sb) {
        final String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            sb.append("[profile] active = default\n");
            return;
        }
        sb.append("[profile] active = ")
                .append(String.join(", ", activeProfiles))
                .append("\n");
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
}
