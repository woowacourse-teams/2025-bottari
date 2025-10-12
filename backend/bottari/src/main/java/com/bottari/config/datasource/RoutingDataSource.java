package com.bottari.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        final boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            log.debug("Routing to DataSource: {}", "replica");
            return DataSourceType.REPLICA;
        }
        log.debug("Routing to DataSource: {}", "master");

        return DataSourceType.MASTER;
    }
}
