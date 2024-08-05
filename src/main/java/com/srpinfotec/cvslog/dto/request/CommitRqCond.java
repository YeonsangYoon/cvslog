package com.srpinfotec.cvslog.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Commit 목록 조회 검색 조건
 */
@Data
public class CommitRqCond {
    private String project; // 프로젝트 이름
    private String user;    // 유저 이름
    private Long page;      // 페이지

    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate endDate;

    public CommitRqCond() {
    }

    public CommitRqCond(String project, String user) {
        this.project = project;
        this.user = user;
    }

    public CommitRqCond(String project, String user, Long page, LocalDate startDate, LocalDate endDate) {
        this.project = project;
        this.user = user;
        this.page = page;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
