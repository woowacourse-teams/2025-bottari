package com.bottari.push;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PushManager {

    private final Map<ChannelType, PushChannel> pushChannels;

    public PushManager(final List<PushChannel> pushChannels) {
        this.pushChannels = pushChannels.stream()
                .collect(Collectors.toMap(
                        PushChannel::channelType,
                        channel -> channel));
    }

    public void unicast(
            final PushMessage message,
            final Long memberId,
            final ChannelType... channelTypes
    ) {
        for (final ChannelType channelType : channelTypes) {
            try {
                unicast(message, memberId, channelType);
            } catch (final UnsupportedOperationException ignore) {
                // 해당 채널 타입이 지원되지 않는 경우 무시하고 다음 채널로 진행
            }
        }
    }

    public void multicast(
            final PushMessage message,
            final List<Long> memberIds,
            final ChannelType... channelType
    ) {
        for (final ChannelType type : channelType) {
            try {
                multicast(message, memberIds, type);
            } catch (final UnsupportedOperationException ignore) {
                // 해당 채널 타입이 지원되지 않는 경우 무시하고 다음 채널로 진행
            }
        }
    }

    public void broadcast(
            final PushMessage message,
            final ChannelType... channelType
    ) {
        for (final ChannelType type : channelType) {
            try {
                broadcast(message, type);
            } catch (final UnsupportedOperationException ignore) {
                // 해당 채널 타입이 지원되지 않는 경우 무시하고 다음 채널로 진행
            }
        }
    }

    private void unicast(
            final PushMessage message,
            final Long memberId,
            final ChannelType channelType
    ) {
        final PushChannel pushChannel = pushChannels.get(channelType);
        if (pushChannel == null) {
            throw new UnsupportedOperationException();
        }
        pushChannel.unicast(message, memberId);
    }

    private void multicast(
            final PushMessage message,
            final List<Long> memberIds,
            final ChannelType channelType
    ) {
        final PushChannel pushChannel = pushChannels.get(channelType);
        if (pushChannel == null) {
            throw new UnsupportedOperationException();
        }
        pushChannel.multicast(message, memberIds);
    }

    private void broadcast(
            final PushMessage message,
            final ChannelType channelType
    ) {
        final PushChannel pushChannel = pushChannels.get(channelType);
        if (pushChannel == null) {
            throw new UnsupportedOperationException();
        }
        pushChannel.broadcast(message);
    }
}
