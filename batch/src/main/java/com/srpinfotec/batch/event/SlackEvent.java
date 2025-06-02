package com.srpinfotec.batch.event;

import com.srpinfotec.batch.slack.SlackMessage;

public record SlackEvent(
        SlackMessage message
) { }
