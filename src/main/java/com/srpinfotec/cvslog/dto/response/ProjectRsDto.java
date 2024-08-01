package com.srpinfotec.cvslog.dto.response;

import lombok.Data;

@Data
public class ProjectRsDto {
    private String projectName;

    public ProjectRsDto(String projectName) {
        this.projectName = projectName;
    }
}
