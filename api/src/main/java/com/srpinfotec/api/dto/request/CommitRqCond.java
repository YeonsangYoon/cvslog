package com.srpinfotec.api.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Commit 목록 조회 검색 조건
 */
@Data
@Builder
public class CommitRqCond {
    private Long project;   // 프로젝트 id
    private Long user;      // 유저 id
    private Long page;      // 페이지

    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate endDate;
}
