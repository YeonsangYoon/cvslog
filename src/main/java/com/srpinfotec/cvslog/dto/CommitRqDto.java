package com.srpinfotec.cvslog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommitRqDto {
    private String commitMsg;
    private String projectName;
    private String userName;
    private LocalDateTime commitTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RevisionRqDto> revisions;
}
