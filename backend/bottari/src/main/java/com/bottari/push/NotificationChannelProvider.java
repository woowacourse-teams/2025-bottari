package com.bottari.push;

import java.util.List;
import java.util.Map;

public interface NotificationChannelProvider {

    ChannelType channel(final Long memberId);

    Map<ChannelType, List<Long>> channel(final List<Long> memberIds);
}
