package com.srpinfotec.cvslog.dto.response;

import lombok.Data;

@Data
public class FetchRsDto {
    private Integer newCommit;  // 새 커밋 개수
    private Integer files;      // 업데이트된 파일 개수

    public FetchRsDto(Integer newCommit, Integer files) {
        this.newCommit = newCommit;
        this.files = files;
    }
}
