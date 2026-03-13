package com.bottari.sse.sse;

public class SseSendTask implements Runnable {

    private final SseChannel sseChannel;
    private final PushMessage pushMessage;
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
