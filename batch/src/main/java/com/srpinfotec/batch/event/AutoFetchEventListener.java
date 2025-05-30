package com.srpinfotec.batch.event;

import com.srpinfotec.batch.BatchConfig;
import com.srpinfotec.batch.exception.BatchException;
import com.srpinfotec.batch.service.FetchService;
import com.srpinfotec.batch.web.response.FetchRsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Component
public class AutoFetchEventListener {
    private final BatchConfig batchConfig;
    private final ApplicationEventPublisher publisher;
    private final FetchService fetchService;

    @Async("AutoFetchEventExecutor")
    @EventListener
    public void handleFetchEvent(AutoFetchEvent event) {
        log.info("Fetch Event execute");

        try {
            JobExecution jobExecution = batchConfig.runDailyFetchCvsLog();
            FetchRsDto result = fetchService.fetchJobExecutionToDto(jobExecution);

            Long fetchCount = result.getFetchCount();
            String fetchStatus = result.getFetchStatus();
            String lastUpdated = result.getLastUpdated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            publisher.publishEvent(
                    new SlackEvent(MessageFormat.format(
                            """
                                    Commit log 자동 Fetch 성공
                                    Revision 수 : {0}
                                    결과 : {1}
                                    수집 시간 : {2}
                                    """
                            , fetchCount.toString(), fetchStatus, lastUpdated)
                    )
            );
        } catch (Exception e) {
            publisher.publishEvent(
                    new SlackEvent("Commit log 자동 Fetch 실패 : " + e.getMessage())
            );

            throw new BatchException("job 실행 에러");
        }
    }
}
