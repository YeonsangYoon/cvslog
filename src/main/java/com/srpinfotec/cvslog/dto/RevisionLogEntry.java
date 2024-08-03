package com.srpinfotec.cvslog.dto;

import com.srpinfotec.cvslog.domain.RevisionType;
import com.srpinfotec.cvslog.error.CustomException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CVS Commit Log의 내용에 대한 객체
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevisionLogEntry {
    private RevisionType type;
    private String filename;
    private String filepath;
    private String username;
    private Long version;
    private LocalDateTime date;
    private String projectName;


    // M 2024-07-01 02:55 +0000 tom    1.7   TAB0101_W05.xml                        SRP-DEV_FAC_v1.0/WebContent/ui/ws/fac_tab            == <remote>
    /**
     * Commit History log 한줄을 parsing 하여 Revision 객체로 변환
     * TODO 유효성 검사 추가
     * @param log 로그
     * @return RevisionLogEntry
     */
    public static RevisionLogEntry parseLog(String log){
        RevisionLogEntry entry = new RevisionLogEntry();

        String[] data = log.split("\\s+");  // log 정보

        if(data.length != 10){
            throw new CustomException("Revision Log 형식 오류(split 개수 차이)");
        }

        // 타입
        entry.setType(RevisionType.getType(data[0]));

        // 수정 시각
        LocalDateTime date = LocalDateTime.parse(data[1] + " " + data[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        entry.setDate(date);

        // 수정자
        entry.setUsername(data[4]);

        // 버전
        entry.setVersion(Long.parseLong(data[5].substring(2)));

        // 파일 이름
        entry.setFilename(data[6]);

        // 파일 경로
        entry.setFilepath(data[7]);

        // 프로젝트 이름
        String[] pathPart = data[7].split("/");
        entry.setProjectName(pathPart[0]);

        return entry;
    }
}
