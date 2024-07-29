package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    @Query("select f from File f " +
            "where f.name = :filename and " +
            "f.path = :filepath and " +
            "f.project.id = :projectId ")
    Optional<File> findByLog(
            @Param("filename") String filename,
            @Param("filepath") String filepath,
            @Param("projectId") Long projectId
    );
}
