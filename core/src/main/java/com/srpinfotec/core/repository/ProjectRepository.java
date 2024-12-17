package com.srpinfotec.core.repository;

import com.srpinfotec.core.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);

    @Query("select p from Project p where p.isUse = 'USE' order by p.name")
    List<Project> findOrderByName();
}
