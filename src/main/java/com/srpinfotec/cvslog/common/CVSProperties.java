package com.srpinfotec.cvslog.common;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cvs")
public class CVSProperties {
    private final String root;
    private final String logFilePath;
    private final String scriptDir;

    public CVSProperties(String root, String workDir, String logFilePath, String scriptDir) {
        this.root = root;
        this.logFilePath = logFilePath;
        this.scriptDir = scriptDir;
    }

    /**
     * Cvs Fetch결과 저장 파일 경로
     * ~/cvs_fetch_140.log
     * @param id 파일 식별자
     * @return 파일 절대 경로
     */
    public String getLogFilePath(Long id){
        return this.getLogFilePath() + "/cvs_fetch_" + id + ".log";
    }
}