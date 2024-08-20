package com.srpinfotec.cvslog.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srpinfotec.cvslog.domain.Project;
import com.srpinfotec.cvslog.dto.response.ProjectRsDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.srpinfotec.cvslog.domain.QProject.project;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Project> findByNaturalId(Object name) {
        return entityManager.unwrap(Session.class)
                .byNaturalId(Project.class)
                .using("name", name)
                .loadOptional();
    }

    public List<ProjectRsDto> findAllDtos(){
        return queryFactory
                .select(Projections.constructor(ProjectRsDto.class,
                        project.id,
                        project.name))
                .from(project)
                .orderBy(project.name.asc())
                .fetch();
    }
}
