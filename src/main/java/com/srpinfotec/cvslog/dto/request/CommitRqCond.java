package com.srpinfotec.cvslog.dto.request;

import lombok.Data;

/**
 * Commit 목록 조회 검색 조건
 */
@Data
public class CommitRqCond {
    private String project; // 프로젝트 이름
    private String user;    // 유저 이름
    private Long page;      // 페이지

    public CommitRqCond() {
    }

    public CommitRqCond(String project, String user) {
        this.project = project;
        this.user = user;
    }

    public CommitRqCond(String project, String user, Long page) {
        this.project = project;
        this.user = user;
        this.page = page;
    }
}
