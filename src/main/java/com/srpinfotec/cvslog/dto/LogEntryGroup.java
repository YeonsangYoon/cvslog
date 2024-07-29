package com.srpinfotec.cvslog.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * RevisionLog를 Commit별로 Grouping 하는데 사용되는 객체
 */
@Data
public class LogEntryGroup {
    private LocalDateTime date;
    private String projectName;
    private String username;

    public LogEntryGroup(LocalDateTime date, String projectName, String username) {
        this.date = date;
        this.projectName = projectName;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEntryGroup that = (LogEntryGroup) o;
        return Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getProjectName(), that.getProjectName()) &&
                Objects.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getProjectName(), getUsername());
    }
}
