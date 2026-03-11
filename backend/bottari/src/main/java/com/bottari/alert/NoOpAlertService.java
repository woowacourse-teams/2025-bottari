package com.bottari.alert;

import org.springframework.stereotype.Service;

@Service
public class NoOpAlertService implements AlertService {

    @Override
    public void send(final String message) {
        // No-op implementation, does nothing
    }
}
