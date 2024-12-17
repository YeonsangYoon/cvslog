package com.srpinfotec.api.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srpinfotec.api.dto.request.CommitRqCond;
import com.srpinfotec.api.dto.response.CommitRsDto;
import com.srpinfotec.api.dto.response.RevisionRsDto;
import com.srpinfotec.core.entity.Commit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.srpinfotec.core.entity.QCommit.commit;
import static com.srpinfotec.core.entity.QFile.file;
import static com.srpinfotec.core.entity.QProject.project;
import static com.srpinfotec.core.entity.QRevision.revision;
import static com.srpinfotec.core.entity.QUser.user;
import static java.util.stream.Collectors.groupingBy;

@Repository
@RequiredArgsConstructor
public class CommitQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<CommitRsDto> findCommitDtoWithoutRevision(CommitRqCond cond){
        return queryFactory
                .select(Projections.constructor(CommitRsDto.class,
                        commit.id,
                        commit.commitMsg,
                        project.name,
                        user.name,
                        JPAExpressions.select(revision.count())
                                .from(revision)
                                .where(revision.commit.eq(commit)),
                        commit.commitTime
                ))
                .from(commit)
                .innerJoin(commit.project, project).fetchJoin()
                .innerJoin(commit.user, user).fetchJoin()
                .where(commitSearchCondition(cond))
                .orderBy(commit.commitTime.desc())
                .fetch();
    }

    public List<CommitRsDto> findCommitDto(CommitRqCond cond){
        List<CommitRsDto> commitRsDtos = queryFactory
                .select(Projections.constructor(CommitRsDto.class,
                        commit.id,
                        commit.commitMsg,
                        commit.project.name,
                        commit.user.name,
                        commit.commitTime
                ))
                .from(commit)
                .innerJoin(commit.project, project)
                .innerJoin(commit.user, user)
                .where(commitSearchCondition(cond))
                .orderBy(commit.commitTime.desc())
                .fetch();

        Map<Long, List<RevisionRsDto>> revisionMap = queryFactory
                .select(Projections.constructor(RevisionRsDto.class,
                        revision.type,
                        revision.version,
                        revision.file.name,
                        revision.file.path,
                        revision.commit.id
                ))
                .from(revision)
                .innerJoin(revision.file, file)
                .where(revision.commit.id.in(
                        commitRsDtos.stream().map(CommitRsDto::getCommitId).toList()
                ))
                .fetch()
                .stream().collect(groupingBy(RevisionRsDto::getCommitId));

        commitRsDtos.forEach(commitRsDto -> {
            commitRsDto.setRevisions(revisionMap.get(commitRsDto.getCommitId()));
            commitRsDto.setRevisionCount((long) commitRsDto.getRevisions().size());
        });

        return commitRsDtos;
    }

    public List<Commit> findTodayCommit() {
        return queryFactory
                .selectFrom(commit)
                .innerJoin(commit.project, project).fetchJoin()
                .innerJoin(commit.user, user).fetchJoin()
                .where(dateGoe(LocalDate.now()))
                .fetch();
    }

    /******************************************************************************************
     * 검색 조건
     ******************************************************************************************/
    private BooleanBuilder commitSearchCondition(CommitRqCond cond){
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(projectIdEq(cond.getProject()))
                .and(userIdEq(cond.getUser()))
                .and(dateGoe(cond.getStartDate()))
                .and(dateLoe(cond.getEndDate()));

        return builder;
    }

    private BooleanExpression projectIdEq(Long projectId){
        return (projectId == null) ? null : project.id.eq(projectId);
    }

    private BooleanExpression userIdEq(Long userId){
        return (userId == null) ? null : user.id.eq(userId);
    }

    private BooleanExpression dateGoe(LocalDate startDate){
        return (startDate != null) ? commit.commitTime.goe(startDate.atTime(LocalTime.MIN)) : null;
    }

    private BooleanExpression dateLoe(LocalDate endDate){
        return (endDate != null) ? commit.commitTime.loe(endDate.atTime(LocalTime.MAX)) : null;
    }
}
