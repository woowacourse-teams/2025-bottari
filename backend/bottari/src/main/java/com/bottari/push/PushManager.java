package com.bottari.push;

import com.bottari.push.connection.ConnectionChannels;
import com.bottari.push.message.PushMessage;
import com.bottari.push.notification.NotificationChannels;
import java.util.ArrayList;
import java.util.Collection;
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
                .unicast()
                .viaConnection(channelTypes[0])
                .viaNotification()
                .send();
    }

    public void multicast(
            final PushMessage message,
            final List<Long> memberIds,
            final ChannelType... channelTypes
    ) {
        message(message)
                .to(memberIds)
                .multicast()
                .viaConnection(channelTypes[0])
                .viaNotification()
                .send();
    }

    public void broadcast(
            final PushMessage message,
            final ChannelType... channelTypes
    ) {
        message(message)
                .broadcast()
                .viaConnection(channelTypes[0])
                .viaNotification()
                .send();
    }
    //endregion

    public StartChain message(final PushMessage message) {
        return new StartChain(message);
    }

    public final class StartChain {

        private final PushMessage message;
        private final List<Long> memberIds = new ArrayList<>();

        public StartChain(final PushMessage message) {
            this.message = message;
        }

        public StartChain to(final Long memberId) {
            this.memberIds.add(memberId);
            return this;
        }

        public StartChain to(final Collection<Long> memberIds) {
            this.memberIds.addAll(memberIds);
            return this;
        }

        public UnicastChain unicast() {
            return new UnicastChain(message, memberIds);
        }

        public MulticastChain multicast() {
            return new MulticastChain(message, memberIds);
        }

        public BroadcastChain broadcast() {
            return new BroadcastChain(message);
        }
    }

    private abstract class ChannelChain {

        private final List<Runnable> actions = new ArrayList<>();
        private final PushMessage message;
        private final List<Long> memberIds;

        public ChannelChain(
                final PushMessage message,
                final List<Long> memberIds
        ) {
            this.message = message;
            this.memberIds = memberIds;
        }

        abstract void executeConnection(
                final PushMessage message,
                final List<Long> memberIds,
                final ChannelType channelType
        );

        abstract void executeNotification(
                final PushMessage message,
                final List<Long> memberIds
        );

        public ChannelChain viaConnection(final ChannelType channelType) {
            actions.add(() -> executeConnection(message, memberIds, channelType));
            return this;
        }

        public ChannelChain viaNotification() {
            actions.add(() -> executeNotification(message, memberIds));
            return this;
        }

        public void send() {
            new ChainExecutor(actions).execute();
        }
    }

    public final class UnicastChain extends ChannelChain {

        public UnicastChain(
                final PushMessage message,
                final List<Long> memberIds
        ) {
            super(message, memberIds);
        }

        @Override
        void executeConnection(
                final PushMessage message,
                final List<Long> memberIds,
                final ChannelType channelType
        ) {
            connectionChannels.unicast(message, channelType, memberIds.getFirst());
        }

        @Override
        void executeNotification(
                final PushMessage message,
                final List<Long> memberIds
        ) {
            notificationChannels.unicast(message, memberIds.getFirst());
        }
    }

    public final class MulticastChain extends ChannelChain {

        public MulticastChain(
                final PushMessage message,
                final List<Long> memberIds
        ) {
            super(message, memberIds);
        }

        @Override
        void executeConnection(
                final PushMessage message,
                final List<Long> memberIds,
                final ChannelType channelType
        ) {
            connectionChannels.multicast(message, channelType, memberIds);
        }

        @Override
        void executeNotification(
                final PushMessage message,
                final List<Long> memberIds
        ) {
            notificationChannels.multicast(message, memberIds);
        }

    }

    public final class BroadcastChain extends ChannelChain {

        public BroadcastChain(final PushMessage message) {
            super(message, List.of());
        }

        @Override
        void executeConnection(
                final PushMessage message,
                final List<Long> memberIds,
                final ChannelType channelType
        ) {
            connectionChannels.broadcast(message, channelType);
        }

        @Override
        void executeNotification(
                final PushMessage message,
                final List<Long> memberIds
        ) {
            throw new UnsupportedOperationException();
        }
    }

    public final class ChainExecutor implements MultiRunnableExecutor {

        private final List<Runnable> actions;

        public ChainExecutor(final List<Runnable> actions) {
            this.actions = actions;
        }

        @Override
        public void execute() {
            actions.forEach(Runnable::run);
        }

        @Override
        public void executeAsync() {
            throw new UnsupportedOperationException();
        }
    }
}
