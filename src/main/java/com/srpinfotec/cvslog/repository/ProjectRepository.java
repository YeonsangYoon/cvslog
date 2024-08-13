package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
    Optional<Project> findByName(String name);

    @Query("select p from Project p order by p.name")
    List<Project> findOrderByName();
}
