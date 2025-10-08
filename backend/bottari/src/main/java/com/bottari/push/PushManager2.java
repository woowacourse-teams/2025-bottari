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
public class PushManager2 {

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

        public ChannelChain unicast() {
            return new ChannelChain(new UnicastExecutor(), message, memberIds);
        }

        public ChannelChain multicast() {
            return new ChannelChain(new MulticastExecutor(), message, memberIds);
        }

        public ChannelChain broadcast() {
            return new ChannelChain(new BroadcastExecutor(), message, List.of());
        }
    }

    public class ChannelChain {

        private final ChannelExecutor channelExecutor;
        private final PushMessage message;
        private final List<Long> memberIds;

        private final List<Runnable> actions = new ArrayList<>();

        public ChannelChain(
                final ChannelExecutor channelExecutor,
                final PushMessage message,
                final List<Long> memberIds
        ) {
            this.channelExecutor = channelExecutor;
            this.message = message;
            this.memberIds = memberIds;
        }

        public ChannelChain viaConnection(final ChannelType channelType) {
            actions.add(() -> channelExecutor.executeConnection(message, memberIds, channelType));
            return this;
        }

        public ChannelChain viaNotification() {
            actions.add(() -> channelExecutor.executeNotification(message, memberIds));
            return this;
        }

        public void send() {
            new ChainExecutor(actions).execute();
        }
    }

    public interface ChannelExecutor {

        void executeConnection(
                final PushMessage message,
                final List<Long> memberIds,
                final ChannelType channelType
        );

        void executeNotification(
                final PushMessage message,
                final List<Long> memberIds
        );
    }

    public final class UnicastExecutor implements ChannelExecutor {

        @Override
        public void executeConnection(
                final PushMessage message,
                final List<Long> memberIds,
                final ChannelType channelType
        ) {
            connectionChannels.unicast(message, channelType, memberIds.getFirst());
        }

        @Override
        public void executeNotification(
                final PushMessage message,
                final List<Long> memberIds
        ) {
            notificationChannels.unicast(message, memberIds.getFirst());
        }
    }

    public final class MulticastExecutor implements ChannelExecutor {

        @Override
        public void executeConnection(
                final PushMessage message,
                final List<Long> memberIds,
                final ChannelType channelType
        ) {
            connectionChannels.multicast(message, channelType, memberIds);
        }

        @Override
        public void executeNotification(
                final PushMessage message,
                final List<Long> memberIds
        ) {
            notificationChannels.multicast(message, memberIds);
        }
    }

    public final class BroadcastExecutor implements ChannelExecutor {

        @Override
        public void executeConnection(
                final PushMessage message,
                final List<Long> memberIds,
                final ChannelType channelType
        ) {
            connectionChannels.broadcast(message, channelType);
        }

        @Override
        public void executeNotification(
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
