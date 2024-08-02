package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.RevisionLog;
import com.srpinfotec.cvslog.dto.LogEntryGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RevisionLogRepository extends JpaRepository<RevisionLog, Long> {

    @Query("select new com.srpinfotec.cvslog.dto.LogEntryGroup(r.date, r.projectName, r.username) " +
            "from RevisionLog r " +
            "group by r.date, r.projectName, r.username")
    Page<LogEntryGroup> findLogGroup(Pageable pageable);
}
