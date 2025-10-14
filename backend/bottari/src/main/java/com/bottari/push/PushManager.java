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

        public ActionStep unicast() {
            return new ChannelChain(new UnicastExecutor(), message, memberIds);
        }

        public ActionStep multicast() {
            return new ChannelChain(new MulticastExecutor(), message, memberIds);
        }

        public ActionStep broadcast() {
            return new ChannelChain(new BroadcastExecutor(), message, List.of());
        }
    }

    public interface ActionStep {
        ActionOrSendStep viaConnection(final ChannelType channelType);
        ActionOrSendStep viaNotification();
    }

    public interface ActionOrSendStep extends ActionStep {
        void send();
    }

    public class ChannelChain implements ActionOrSendStep {

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

        @Override
        public ActionOrSendStep viaConnection(final ChannelType channelType) {
            actions.add(() -> channelExecutor.executeConnection(message, memberIds, channelType));
            return this;
        }
        @Override
        public ActionOrSendStep viaNotification() {
            actions.add(() -> channelExecutor.executeNotification(message, memberIds));
            return this;
        }

        @Override
        public void send() {
            new ChainActionsExecutor().execute(actions);
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

    public static final class ChainActionsExecutor implements MultiRunnableExecutor {

        @Override
        public void execute(final List<Runnable> actions) {
            actions.forEach(Runnable::run);
        }

        @Override
        public void executeAsync(final List<Runnable> actions) {
            throw new UnsupportedOperationException();
        }
    }
}
