package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.Project;
import com.srpinfotec.cvslog.dto.response.ProjectRsDto;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryCustom {
    Optional<Project> findByNaturalId(Object name);

    List<ProjectRsDto> findAllDtos();
}
