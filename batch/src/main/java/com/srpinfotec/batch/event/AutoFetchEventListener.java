package com.srpinfotec.batch.event;

import com.srpinfotec.batch.BatchConfig;
import com.srpinfotec.batch.exception.BatchException;
import com.srpinfotec.batch.service.FetchService;
import com.srpinfotec.batch.slack.SlackMessage;
import com.srpinfotec.batch.web.response.FetchRsDto;
import com.srpinfotec.core.repository.CommitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Component
public class AutoFetchEventListener {
    private final BatchConfig batchConfig;
    private final ApplicationEventPublisher publisher;
    private final FetchService fetchService;
    private final CommitRepository commitRepository;

    @Async("AutoFetchEventExecutor")
    @EventListener
    public void handleFetchEvent(AutoFetchEvent event) {
        log.info("Fetch Event execute");

        try {
            JobExecution jobExecution = batchConfig.runDailyFetchCvsLog();
            FetchRsDto result = fetchService.fetchJobExecutionToDto(jobExecution);

            Long fetchCount = result.getFetchCount();

            if (fetchCount == 0L) {
                return;
            }

            commitRepository.findRecentCommit()
                    .ifPresent(commit -> {
                        publisher.publishEvent(
                                new SlackEvent(
                                        SlackMessage.createCommitAlertMessage(
                                                commit.getUser().getName(),
                                                commit.getCommitMsg(),
                                                commit.getProject().getName(),
                                                fetchCount,
                                                commit.getCommitTime()
                                        )
                                )
                        );
                    });
        } catch (Exception e) {
            publisher.publishEvent(
                    new SlackEvent(
                            SlackMessage.createCommitFailureMessage(
                                    e.getMessage(),
                                    LocalDateTime.now()
                            )
                    )
            );

            throw new BatchException("job 실행 에러");
        }
    }
}
