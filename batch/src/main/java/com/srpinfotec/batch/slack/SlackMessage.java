package com.srpinfotec.batch.slack;

import lombok.*;

@Getter @Setter @ToString
public class SlackMessage {
    private String text;
    private final String username = "cvs bot";
    private final String icon_emoji = ":robot_face:";

    public SlackMessage(String text) {
        this.text = text;
    }
}
