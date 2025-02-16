package com.srpinfotec.core.repository;

import com.srpinfotec.core.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    @Query("select f from " +
            "File f " +
            "join fetch f.project p " +
            "where f.name = :filename and " +
            "f.path = :filepath and " +
            "p.name = :projectName ")
    Optional<File> findByLog(
            @Param("filename") String filename,
            @Param("filepath") String filepath,
            @Param("projectName") String projectName
    );
}
