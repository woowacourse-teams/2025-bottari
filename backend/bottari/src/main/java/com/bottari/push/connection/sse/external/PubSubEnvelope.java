package com.bottari.push.connection.sse.external;

import com.bottari.push.message.PushMessage;
import java.util.Map;

public record PubSubEnvelope(
        Map<String, String> headers,
        PushMessage payload
) {
}
