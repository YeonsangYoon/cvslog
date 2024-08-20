package com.srpinfotec.cvslog.dto.response;

import lombok.Data;

@Data
public class ProjectRsDto {
    private Long projectId;
    private String projectName;

    public ProjectRsDto(String projectName) {
        this.projectName = projectName;
    }

    public ProjectRsDto(Long projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public void addCommitCountToName(int count){
        this.projectName = this.projectName + " (+" + count + ")";
    }
}
