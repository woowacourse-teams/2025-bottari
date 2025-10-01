package com.bottari.push;

import com.bottari.push.message.PushMessage;

public interface ConnectionChannel extends PushChannel {

    void broadcast(final PushMessage message);
}
