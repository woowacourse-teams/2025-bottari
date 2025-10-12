package com.bottari.config.datasource;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile({"dev", "prod"})
@Aspect
@Component
public class RoutingDataSourceAspect {

    @Before("@annotation(transactional)")
    public void setDataSourceType(final Transactional transactional) {
        if (transactional.readOnly()) {
            RoutingDataSource.setDataSourceType(DataSourceType.REPLICA);
            return;
        }
        RoutingDataSource.setDataSourceType(DataSourceType.MASTER);
    }

    @After("@annotation(transactional)")
    public void clearDataSourceType(final Transactional transactional) {
        RoutingDataSource.clear();
    }
}
