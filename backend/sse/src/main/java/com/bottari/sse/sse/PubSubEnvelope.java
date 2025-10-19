package com.bottari.sse.sse;

import java.util.Map;

public record PubSubEnvelope(
        Map<String, String> headers,
        PushMessage payload
) {
}
