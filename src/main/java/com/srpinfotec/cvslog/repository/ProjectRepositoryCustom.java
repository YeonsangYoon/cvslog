package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.Project;

import java.util.Optional;

public interface ProjectRepositoryCustom {
    Optional<Project> findByNaturalId(Object name);
}
