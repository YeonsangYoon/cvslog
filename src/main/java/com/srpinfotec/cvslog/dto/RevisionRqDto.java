package com.srpinfotec.cvslog.dto;

import com.srpinfotec.cvslog.domain.RevisionType;
import lombok.Data;

@Data
public class RevisionRqDto {
    private RevisionType type;
    private Long version;
    private String filename;
    private String filepath;
}
