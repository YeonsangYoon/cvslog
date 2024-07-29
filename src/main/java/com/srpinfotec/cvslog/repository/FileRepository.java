package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

}
