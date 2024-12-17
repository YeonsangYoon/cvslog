package com.srpinfotec.core.repository;

import com.srpinfotec.core.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

}
