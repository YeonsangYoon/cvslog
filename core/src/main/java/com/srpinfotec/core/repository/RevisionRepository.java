package com.srpinfotec.core.repository;

import com.srpinfotec.core.entity.Revision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevisionRepository extends JpaRepository<Revision, Long> {
    List<Revision> findByCommitId(Long commitId);
}
