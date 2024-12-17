package com.srpinfotec.api.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srpinfotec.api.dto.response.ProjectRsDto;
import com.srpinfotec.core.value.UseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.srpinfotec.core.entity.QProject.project;

@Repository
@RequiredArgsConstructor
public class ProjectQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<ProjectRsDto> findAllDtos(){
        return queryFactory
                .select(Projections.constructor(ProjectRsDto.class,
                        project.id,
                        project.name))
                .from(project)
                .where(project.isUse.eq(UseType.USE))
                .orderBy(project.name.asc())
                .fetch();
    }
}
