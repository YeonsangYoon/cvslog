package com.srpinfotec.cvslog.dto.response;

import com.srpinfotec.cvslog.domain.RevisionType;
import lombok.Data;

@Data
public class RevisionRsDto {
    private RevisionType type;
    private Long version;
    private String filename;
    private String filepath;

    public RevisionRsDto(RevisionType type, Long version, String filename, String filepath) {
        this.type = type;
        this.version = version;
        this.filename = filename;
        this.filepath = filepath;
    }
}
