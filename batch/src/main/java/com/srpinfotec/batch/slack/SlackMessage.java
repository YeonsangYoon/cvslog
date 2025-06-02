package com.srpinfotec.batch.slack;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlackMessage {
    private final String username = "cvs bot";
    private final String icon_emoji = ":robot_face:";
    private String text;
    private List<Block> blocks;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Block {
        private String type;
        private Text text;
        private List<Field> fields;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Text {
        private String type;
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Field {
        private String type;
        private String text;
    }

    public static SlackMessage createCommitAlertMessage(
            String author,
            String message,
            String projectName,
            Long revisionCount,
            LocalDateTime dateTime
    ) {
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setText(":memo: *Commit Alert*");

        // 제목 블럭
        SlackMessage.Block titleBlock = new SlackMessage.Block();
        titleBlock.setType("section");
        titleBlock.setText(new SlackMessage.Text("mrkdwn", "*New Commit Pushed*"));

        // 상세 필드 블럭
        SlackMessage.Block detailBlock = new SlackMessage.Block();
        detailBlock.setType("section");
        detailBlock.setFields(List.of(
                new SlackMessage.Field("mrkdwn", "*Author:*\n" + author),
                new SlackMessage.Field("mrkdwn", "*Project:*\n" + projectName),
                new SlackMessage.Field("mrkdwn", "*Message:*\n" + message),
                new SlackMessage.Field("mrkdwn", "*File count:*\n" + revisionCount.toString()),
                new SlackMessage.Field("mrkdwn", "*Date:*\n" + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        ));

        slackMessage.setBlocks(List.of(titleBlock, detailBlock));
        return slackMessage;
    }

    public static SlackMessage createCommitFailureMessage(
            String errorMessage,
            LocalDateTime dateTime
    ) {
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setText(":x: *Commit Processing Failed*");

        // 제목 블럭
        SlackMessage.Block titleBlock = new SlackMessage.Block();
        titleBlock.setType("section");
        titleBlock.setText(new SlackMessage.Text("mrkdwn", ":x: *Commit Processing Failed*"));

        // 상세 필드 블럭
        SlackMessage.Block detailBlock = new SlackMessage.Block();
        detailBlock.setType("section");
        detailBlock.setFields(List.of(
                new SlackMessage.Field("mrkdwn", "*Error:*\n" + errorMessage),
                new SlackMessage.Field("mrkdwn", "*Date:*\n" + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        ));

        slackMessage.setBlocks(List.of(titleBlock, detailBlock));
        return slackMessage;
    }
}