package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectCustomRepository{
    Optional<Project> findByName(String name);
}
