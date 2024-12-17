package com.srpinfotec.core.repository;

import com.srpinfotec.core.entity.Project;
import java.util.Optional;

public interface ProjectRepositoryCustom {
    Optional<Project> findByNaturalId(Object name);
}
