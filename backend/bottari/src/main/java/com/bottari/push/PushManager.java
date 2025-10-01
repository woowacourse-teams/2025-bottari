package com.bottari.push;

import com.bottari.push.message.PushMessage;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PushManager {

    private final NotificationChannels notificationChannels;
    private final ConnectionChannels connectionChannels;

    //region 하위호환 메서드
    public void unicast(
            final PushMessage message,
            final Long memberId,
            final ChannelType... channelTypes
    ) {
        message(message)
                .to(memberId)
                .viaConnection(channelTypes[0])
                .viaNotification()
                .unicast();
    }

    public void multicast(
            final PushMessage message,
            final List<Long> memberIds,
            final ChannelType... channelTypes
    ) {
        message(message)
                .to(memberIds)
                .viaConnection(channelTypes[0])
                .viaNotification()
                .multicast();
    }

    public void broadcast(
            final PushMessage message,
            final ChannelType... channelTypes
    ) {
        message(message)
                .viaConnection(channelTypes[0])
                .viaNotification()
                .broadcast();
    }
    //endregion

    public PushChain message(final PushMessage message) {
        return new PushChain(message);
    }

    public final class PushChain {

        private final PushMessage message;

        private final List<Long> memberIds = new ArrayList<>();

        private boolean viaNotificationCondition = false;
        private ChannelType connectionChannelType;

        public PushChain(final PushMessage message) {
            if (message == null) {
                throw new IllegalArgumentException("메시지는 null 일 수 없습니다.");
            }
            this.message = message;
        }

        public PushChain to(final Long memberId) {
            this.memberIds.add(memberId);

            return this;
        }

        public PushChain to(final List<Long> memberIds) {
            this.memberIds.addAll(memberIds);

            return this;
        }

        public PushChain viaNotification() {
            viaNotificationCondition = true;

            return this;
        }

        public PushChain viaConnection(final ChannelType channelType) {
            this.connectionChannelType = channelType;

            return this;
        }

        public void unicast() {
            validateReceivers();
            final Long memberId = memberIds.getFirst();
            if (viaNotificationCondition) {
                notificationChannels.unicast(message, memberId);
            }
            if (connectionChannelType != null) {
                connectionChannels.unicast(message, connectionChannelType, memberId);
            }
        }

        public void multicast() {
            validateReceivers();
            if (viaNotificationCondition) {
                notificationChannels.multicast(message, memberIds);
            }
            if (connectionChannelType != null) {
                connectionChannels.multicast(message, connectionChannelType, memberIds);
            }
        }

        public void broadcast() {
            if (viaNotificationCondition) {
                throw new UnsupportedOperationException();
            }
            if (connectionChannelType != null) {
                connectionChannels.broadcast(message, connectionChannelType);
            }
        }

        public void send() {
            if (memberIds.size() == 1) {
                unicast();
                return;
            }
            if (memberIds.size() > 1) {
                multicast();
                return;
            }
            broadcast();
        }

        private void validateReceivers() {
            if (memberIds.isEmpty()) {
                throw new IllegalStateException("수신자가 지정되지 않았습니다. to() 메서드를 사용하여 수신자를 지정하세요.");
            }
        }
    }
}
