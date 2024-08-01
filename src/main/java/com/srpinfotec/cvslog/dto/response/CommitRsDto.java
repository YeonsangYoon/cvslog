package com.srpinfotec.cvslog.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommitRsDto {
    private String commitMsg;
    private String projectName;
    private String userName;
    private LocalDateTime commitTime;
    private Long revisionCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RevisionRsDto> revisions;

    @QueryProjection
    public CommitRsDto(String commitMsg, String projectName, String userName, LocalDateTime commitTime, Long revisionCount) {
        this.commitMsg = commitMsg;
        this.projectName = projectName;
        this.userName = userName;
        this.commitTime = commitTime;
        this.revisionCount = revisionCount;
    }

    public CommitRsDto(String commitMsg, String projectName, String userName, LocalDateTime commitTime, Long revisionCount, List<RevisionRsDto> revisions) {
        this.commitMsg = commitMsg;
        this.projectName = projectName;
        this.userName = userName;
        this.commitTime = commitTime;
        this.revisionCount = revisionCount;
        this.revisions = revisions;
    }
}
