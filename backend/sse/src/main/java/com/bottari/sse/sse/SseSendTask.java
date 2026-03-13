package com.bottari.sse.sse;

import lombok.Getter;

public class SseSendTask implements Runnable {
    private final SseChannel sseChannel;

    @Getter
    private final PushMessage pushMessage;
    @Getter
    private final MemberChannelTopic topic;
    public SseSendTask(
            final SseChannel sseChannel,
            final PushMessage pushMessage,
            final MemberChannelTopic topic
    ) {
        this.sseChannel = sseChannel;
        this.pushMessage = pushMessage;
        this.topic = topic;
    }

    @Override
    public void run() {
        final Long memberId = topic.extractMemberId();
        sseChannel.unicast(pushMessage, memberId);
    }
}
