package com.srpinfotec.batch.web.controller;

import com.srpinfotec.batch.web.dto.response.FetchRsDto;
import com.srpinfotec.batch.web.dto.ApiResponse;
import com.srpinfotec.batch.service.FetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class BatchController {
    private final FetchService fetchService;

    /**
     * 오늘자 commit 기록 수집
     */
    @PostMapping("/fetch")
    public ResponseEntity<ApiResponse> fetchDailyCvsLog(){
        FetchRsDto result = fetchService.fetch();

        return ResponseEntity
                .ok(ApiResponse.success(result));
    }

    /**
     * 모든 commit 기록 수집
     */
    @PostMapping("/fetch/all")
    public ResponseEntity<ApiResponse> fetchAllLog(){
        fetchService.fetchAll();

        return ResponseEntity
                .ok(ApiResponse.success(null));
    }

    /**
     * 최근 fetch 기록 반환
     */
    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse> lastUpdateFetch(){
        FetchRsDto recentResult = fetchService.getRecentFetchResult();

        return ResponseEntity
                .ok(ApiResponse.success(recentResult));
    }
}
