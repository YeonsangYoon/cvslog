package com.srpinfotec.batch.event;

import com.srpinfotec.batch.BatchConfig;
import com.srpinfotec.batch.exception.BatchException;
import com.srpinfotec.batch.service.FetchService;
import com.srpinfotec.batch.slack.SlackMessage;
import com.srpinfotec.batch.web.response.FetchRsDto;
import com.srpinfotec.core.entity.Commit;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class AutoFetchEventListener {
    private final BatchConfig batchConfig;
    private final ApplicationEventPublisher publisher;
    private final FetchService fetchService;
    private final EntityManager entityManager;

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

            publisher.publishEvent(new SlackEvent(
                    fetchService.getRecentCommitSlackMessage(fetchCount)
            ));
        } catch (Exception e) {
            log.error(e.getMessage());

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
