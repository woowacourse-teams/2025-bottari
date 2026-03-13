package com.bottari.sse.sse;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SseExecutorRejectedHandler implements RejectedExecutionHandler {

    private final SseRefreshRegistry registry;

    @Override
    public void rejectedExecution(
            final Runnable r,
            final ThreadPoolExecutor executor
    ) {
        if (r instanceof SseSendTask) {
            final SseSendTask task = (SseSendTask) r;
            final MemberChannelTopic topic = task.getTopic();
            final Long rejectedMemberId = topic.extractMemberId();
            registry.setFlag(rejectedMemberId);
        }
    }
}
