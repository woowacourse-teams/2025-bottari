package com.bottari.push.fcm.dto;

import com.bottari.push.ChannelType;
import com.bottari.push.PushMessage;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SendMessageRequest implements PushMessage {

    private final Map<String, String> data;
    private final MessageType messageType;

    @Override
    public ChannelType channelType() {
        return ChannelType.FCM;
    }
}
