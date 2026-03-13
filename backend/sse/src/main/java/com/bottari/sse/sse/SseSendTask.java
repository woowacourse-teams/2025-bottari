package com.bottari.sse.sse;

import lombok.Getter;

public class SseSendTask implements Runnable {

    private final SseChannel sseChannel;
    private final LatestMessageRegistry registry;

    @Getter
    private final PushMessage pushMessage;

    @Getter
    private final MemberChannelTopic topic;

    @Getter
    private final DeliveryKey deliveryKey;

    public SseSendTask(
            final SseChannel sseChannel,
            final LatestMessageRegistry registry,
            final PushMessage pushMessage,
            final MemberChannelTopic topic,
            final DeliveryKey deliveryKey
    ) {
        this.sseChannel = sseChannel;
        this.registry = registry;
        this.pushMessage = pushMessage;
        this.topic = topic;
        this.deliveryKey = deliveryKey;
    }

    @Override
    public void run() {
        final Long memberId = topic.extractMemberId();
        if (registry.removeIfSame(deliveryKey, pushMessage.publishedAt())) {
            sseChannel.unicast(pushMessage, memberId);
        }
    }
}
