package com.srpinfotec.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.srpinfotec.api.util.TimeUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommitRsDto {
    private Long commitId;
    private String commitMsg;
    private String projectName;
    private String userName;
    private Long revisionCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime commitTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RevisionRsDto> revisions;


    public CommitRsDto(Long commitId, String commitMsg, String projectName, String userName, LocalDateTime commitTime) {
        this.commitId = commitId;
        this.commitMsg = commitMsg;
        this.projectName = projectName;
        this.userName = userName;
        this.commitTime = TimeUtil.changeTimeZoneUtcToAsia(commitTime);
    }

    public CommitRsDto(Long commitId, String commitMsg, String projectName, String userName, Long revisionCount, LocalDateTime commitTime) {
        this.commitId = commitId;
        this.commitMsg = commitMsg;
        this.projectName = projectName;
        this.userName = userName;
        this.commitTime = TimeUtil.changeTimeZoneUtcToAsia(commitTime);
        this.revisionCount = revisionCount;
    }

    public CommitRsDto(Long commitId, String commitMsg, String projectName, String userName, LocalDateTime commitTime, Long revisionCount, List<RevisionRsDto> revisions) {
        this.commitId = commitId;
        this.commitMsg = commitMsg;
        this.projectName = projectName;
        this.userName = userName;
        this.commitTime = TimeUtil.changeTimeZoneUtcToAsia(commitTime);
        this.revisionCount = revisionCount;
        this.revisions = revisions;
    }
}
