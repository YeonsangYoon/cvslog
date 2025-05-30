package com.srpinfotec.batch.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
@RequiredArgsConstructor
public class SlackNotifier {
    @Value("${slack.webhook-url}")
    private String slackWebhookUrl;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public void sendMessage(String message) {
        try {
            String payload = objectMapper.writeValueAsString(new SlackMessage(message));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(payload, headers);

            restTemplate.postForEntity(slackWebhookUrl, entity, String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Slack message json parsing error", e);
        }
    }
}
