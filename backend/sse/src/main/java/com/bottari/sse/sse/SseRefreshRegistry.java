package com.bottari.sse.sse;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SseRefreshRegistry {

    private final Set<Long> memberIdsToRefresh = ConcurrentHashMap.newKeySet();

    public void setFlag(final Long memberId) {
        memberIdsToRefresh.add(memberId);
    }

    public boolean needRefresh(final Long memberId) {
        return memberIdsToRefresh.remove(memberId);
    }
}
