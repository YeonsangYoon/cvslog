package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.RevisionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevisionLogRepository extends JpaRepository<RevisionLog, Long> {
}
