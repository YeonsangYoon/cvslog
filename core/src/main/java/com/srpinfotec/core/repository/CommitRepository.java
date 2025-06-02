package com.srpinfotec.core.repository;

import com.srpinfotec.core.entity.Commit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CommitRepository extends JpaRepository<Commit, Long>{
    @Query("select c " +
            "from Commit c " +
            "join fetch c.project p " +
            "join fetch c.user u " +
            "where c.commitTime = :commitTime " +
            "and p.name = :projectName " +
            "and u.name = :userName")
    Optional<Commit> findByRevision(
            @Param("commitTime") LocalDateTime commitTime,
            @Param("projectName") String projectName,
            @Param("userName") String userName
    );

    @Query("select c from Commit c where c.commitMsg is null")
    Page<Commit> findCommitMsgIsNull(Pageable pageable);

    @Query("select c from Commit c where c.id = (select max(cc.id) from Commit cc)")
    Optional<Commit> findRecentCommit();
}
