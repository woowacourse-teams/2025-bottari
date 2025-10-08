package com.bottari.push.connection;

import com.bottari.push.PushChannel;
import com.bottari.push.message.PushMessage;

public interface ConnectionChannel extends PushChannel {

    void broadcast(final PushMessage message);
}
