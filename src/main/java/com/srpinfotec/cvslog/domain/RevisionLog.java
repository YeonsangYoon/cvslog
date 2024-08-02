package com.srpinfotec.cvslog.domain;

import com.srpinfotec.cvslog.dto.RevisionLogEntry;
import com.srpinfotec.cvslog.error.CustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @ToString(of = {"type", "filename", "filepath", "username", "version", "date", "projectName"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CVS_REVISION_LOG")
public class RevisionLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_ID")
    private Long id;

    @Column(name = "REVISION_TYPE")
    private RevisionType type;

    @Column(name = "FILE_NAME")
    private String filename;

    @Column(name = "FILE_PATH")
    private String filepath;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "VERSION")
    private Long version;

    @Column(name = "COMMIT_TIME")
    private LocalDateTime date;

    @Column(name = "PROJECT_NAME")
    private String projectName;

    public RevisionLog(RevisionType type, String filename, String filepath, String username, Long version, LocalDateTime date, String projectName) {
        this.type = type;
        this.filename = filename;
        this.filepath = filepath;
        this.username = username;
        this.version = version;
        this.date = date;
        this.projectName = projectName;
    }

    public static RevisionLog parseLog(String log){
        String[] data = log.split("\\s+");  // log 정보

        if(data.length != 10){
            throw new CustomException("Revision Log 형식 오류(split 개수 차이)");
        }

        LocalDateTime date = LocalDateTime.parse(data[1] + " " + data[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String[] pathPart = data[7].split("/");

        return new RevisionLog(
                RevisionType.getType(data[0]),
                data[6],
                data[7],
                data[4],
                Long.parseLong(data[5].substring(2)),
                date,
                pathPart[0]
        );
    }
}
