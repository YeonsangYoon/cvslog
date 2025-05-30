package com.srpinfotec.batch.event;

import com.srpinfotec.batch.slack.SlackNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SlackEventListener {
    private final SlackNotifier slackNotifier;

    @Async("SlackEventExecutor")
    @EventListener
    public void handleFetchEvent(SlackEvent event){
        log.info("send slack message");

        slackNotifier.sendMessage(event.message());
    }
}
