package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CommitRepository extends JpaRepository<Commit, Long>, CommitRepositoryCustom {
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
}
