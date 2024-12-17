package com.srpinfotec.batch.job.dto;

import lombok.Data;

@Data
public class LogBuffer {
    private String versionLine;
    private String updateInfoLine;
    private String message;

    public Long getVersion() {
        String[] tokens = versionLine.split("\\.");

        if (tokens.length < 2) return -1L;  // version line 패턴

        return Long.parseLong(tokens[1]);
    }
}
