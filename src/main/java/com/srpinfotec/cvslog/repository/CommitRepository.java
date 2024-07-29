package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitRepository extends JpaRepository<Commit, Long> {
}
