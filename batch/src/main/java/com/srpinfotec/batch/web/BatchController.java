package com.srpinfotec.batch.web;

import com.srpinfotec.batch.event.AutoFetchEvent;
import com.srpinfotec.batch.exception.BatchException;
import com.srpinfotec.batch.web.response.FetchRsDto;
import com.srpinfotec.batch.service.FetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchController {
    private final FetchService fetchService;
    private final ApplicationEventPublisher publisher;
    private final JobExplorer jobExplorer;

    /**
     * 오늘자 commit 기록 수집
     */
    @PostMapping("/fetch")
    public ResponseEntity<ApiResponse<FetchRsDto>> fetchDailyCvsLog() {
        log.info("fetch daily cvs log manually");

        FetchRsDto result = fetchService.manualFetch();

        return ResponseEntity
                .ok(ApiResponse.success(result));
    }

    @PostMapping("/fetch-auto")
    public ResponseEntity<?> fetchByAutoWebhook() {
        log.info("fetch by auto webhook");
        publisher.publishEvent(new AutoFetchEvent());
        return ResponseEntity.ok(null);
    }

    /**
     * 최근 월 단위 commit 기록 수집
     */
    @PostMapping("/fetch/recent/{month}")
    public ResponseEntity<ApiResponse<FetchRsDto>> fetchRecentLog(
            @PathVariable(required = false) Integer month
    ) {
        if (month == null) {
            month = 1;
        }

        log.info("fetch recent {} month cvs log", month);

        FetchRsDto result = fetchService.fetchRecentMonth(month);

        return ResponseEntity
                .ok(ApiResponse.success(result));
    }

    /**
     * 최근 fetch 기록 반환
     */
    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<FetchRsDto>> lastUpdateFetch() {
        FetchRsDto recentResult = fetchService.getRecentFetchResult();

        return ResponseEntity
                .ok(ApiResponse.success(recentResult));
    }
}
