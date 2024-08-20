package com.srpinfotec.cvslog.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Commit 목록 조회 검색 조건
 */
@Data
public class CommitRqCond {
    private Long project;   // 프로젝트 id
    private Long user;      // 유저 id
    private Long page;      // 페이지

    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate endDate;

    public CommitRqCond() {
    }

    public CommitRqCond(Long projectId, Long userId) {
        this.project = projectId;
        this.user = userId;
    }

    public CommitRqCond(Long projectId, Long userId, Long page, LocalDate startDate, LocalDate endDate) {
        this.project = projectId;
        this.user = userId;
        this.page = page;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
