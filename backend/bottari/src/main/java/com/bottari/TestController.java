package com.bottari;

import com.bottari.push.ChannelType;
import com.bottari.push.PushManager;
import com.bottari.push.message.MessageEventType;
import com.bottari.push.message.MessageResourceType;
import com.bottari.push.message.PushMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final PushManager pushManager;

    @GetMapping("/test/push/{memberId}")
    public void testPush(@PathVariable Long memberId) {
        pushManager.message(provideTestMessage())
                .to(memberId)
                .unicast()
                .viaConnection(ChannelType.SSE)
                .send();
    }

    private PushMessage provideTestMessage() {
        return new PushMessage(
                MessageResourceType.SHARED_ITEM_INFO,
                MessageEventType.REMIND,
                Map.of()
        );
    }
}
