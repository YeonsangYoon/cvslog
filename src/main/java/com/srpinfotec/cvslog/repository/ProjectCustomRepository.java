package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.Project;

import java.util.Optional;

public interface ProjectCustomRepository {
    Optional<Project> findByNaturalId(Object name);
}
