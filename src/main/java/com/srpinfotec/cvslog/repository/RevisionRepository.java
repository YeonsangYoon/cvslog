package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.Revision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RevisionRepository extends JpaRepository<Revision, Long> {
    @Query("select r from Revision r " +
                "join fetch r.file f " +
            "where f.name = :filename and f.path = :filepath and r.version = :version")
    Optional<Revision> findByLog(
            @Param("filename") String filename,
            @Param("filepath") String filepath,
            @Param("version") Long version
    );
}
