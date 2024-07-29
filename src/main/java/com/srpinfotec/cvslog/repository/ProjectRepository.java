package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
