package com.srpinfotec.cvslog.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srpinfotec.cvslog.domain.Commit;
import com.srpinfotec.cvslog.dto.request.CommitRqCond;
import com.srpinfotec.cvslog.dto.response.CommitRsDto;
import com.srpinfotec.cvslog.dto.response.QCommitRsDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.srpinfotec.cvslog.domain.QCommit.commit;
import static com.srpinfotec.cvslog.domain.QProject.project;
import static com.srpinfotec.cvslog.domain.QRevision.revision;
import static com.srpinfotec.cvslog.domain.QUser.user;
import static org.springframework.util.StringUtils.hasLength;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommitRepositoryImpl implements CommitCustomRepository{
    private static final Long COMMIT_LIMIT = 100L;   //한 페이지당 commit 최대 개수

    private final EntityManager em;
    private final EntityManagerFactory emf;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommitRsDto> findCommitDtoWithoutRevision(){
        return queryFactory
                .select(new QCommitRsDto(
                        commit.commitMsg,
                        project.name,
                        user.name,
                        commit.commitTime,
                        JPAExpressions.select(revision.count())
                                .from(revision)
                                .where(revision.commit.eq(commit))
                ))
                .from(commit)
                .innerJoin(commit.project, project)
                .innerJoin(commit.user, user)
                .fetch();
    }

    @Override
    public List<CommitRsDto> findCommitDto(CommitRqCond cond){
        List<Commit> commits = queryFactory
                .selectFrom(commit)
                .innerJoin(commit.user, user).fetchJoin()
                .innerJoin(commit.project, project).fetchJoin()
                .where(commitSearchCondition(cond))
                .orderBy(commit.commitTime.desc())
                .fetch();

        return commits.stream().map(Commit::toRsDto).toList();
    }

    @Override
    public List<CommitRsDto> findCommmitDtoByPage(CommitRqCond cond) {
        List<Commit> commits = queryFactory
                .selectFrom(commit)
                .innerJoin(commit.user, user).fetchJoin()
                .innerJoin(commit.project, project).fetchJoin()
                .where(commitSearchCondition(cond))
                .orderBy(commit.commitTime.desc())
                .offset((cond.getPage() - 1) * COMMIT_LIMIT)
                .limit(COMMIT_LIMIT)
                .fetch();

        return commits.stream().map(Commit::toRsDto).toList();
    }

    /******************************************************************************************
     * 검색 조건
     ******************************************************************************************/
    private BooleanBuilder commitSearchCondition(CommitRqCond cond){
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(projectNameEq(cond.getProject()))
                .and(userNameEq(cond.getUser()))
                .and(dateGoe(cond.getStartDate()))
                .and(dateLoe(cond.getEndDate()));

        return builder;
    }

    private BooleanExpression projectNameEq(String projectName){
        return hasLength(projectName) ? project.name.equalsIgnoreCase(projectName) : null;
    }

    private BooleanExpression userNameEq(String userName){
        return hasLength(userName) ? user.name.equalsIgnoreCase(userName) : null;
    }

    private BooleanExpression dateGoe(LocalDate startDate){
        return (startDate != null) ? commit.commitTime.goe(startDate.atTime(LocalTime.MIN)) : null;
    }

    private BooleanExpression dateLoe(LocalDate endDate){
        return (endDate != null) ? commit.commitTime.loe(endDate.atTime(LocalTime.MAX)) : null;
    }
}
