package com.srpinfotec.cvslog.common;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cvs")
public class CVSProperties {
    private final String root;
    private final String workDir;
    private final String logFilePath;

    public CVSProperties(String root, String workDir, String logFilePath) {
        this.root = root;
        this.workDir = workDir;
        this.logFilePath = logFilePath;
    }
}