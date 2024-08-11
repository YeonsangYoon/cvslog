package com.srpinfotec.cvslog.controller;

import com.srpinfotec.cvslog.dto.ResponseDto;
import com.srpinfotec.cvslog.dto.request.FetchRqCond;
import com.srpinfotec.cvslog.dto.response.FetchRsDto;
import com.srpinfotec.cvslog.service.FetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BatchController {
    private final FetchService fetchService;


    @PostMapping("/fetch")
    public ResponseEntity<ResponseDto> fetchDailyCvsLog(@RequestBody FetchRqCond cond){
        FetchRsDto result = fetchService.fetch(cond);

        return ResponseEntity
                .ok(ResponseDto.success(result));
    }

    @GetMapping("/fetch")
    public ResponseEntity<ResponseDto> lastUpdateFetch(){
        FetchRsDto recentResult = fetchService.getRecentFetchResult();

        return ResponseEntity
                .ok(ResponseDto.success(recentResult));
    }
}
