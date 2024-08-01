package com.srpinfotec.cvslog.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srpinfotec.cvslog.domain.Commit;
import com.srpinfotec.cvslog.domain.Revision;
import com.srpinfotec.cvslog.dto.request.CommitRqCond;
import com.srpinfotec.cvslog.dto.response.CommitRsDto;
import com.srpinfotec.cvslog.dto.response.QCommitRsDto;
import com.srpinfotec.cvslog.dto.response.RevisionRsDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.srpinfotec.cvslog.domain.QCommit.commit;
import static com.srpinfotec.cvslog.domain.QFile.file;
import static com.srpinfotec.cvslog.domain.QProject.project;
import static com.srpinfotec.cvslog.domain.QRevision.revision;
import static com.srpinfotec.cvslog.domain.QUser.user;
import static org.springframework.util.StringUtils.hasLength;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommitRepositoryImpl implements CommitCustomRepository{
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
        List<Revision> revisions = queryFactory
                .selectFrom(revision)
                .innerJoin(revision.commit, commit).fetchJoin()
                .innerJoin(commit.project, project).fetchJoin()
                .innerJoin(commit.user, user).fetchJoin()
                .innerJoin(revision.file, file).fetchJoin()
                .where(commitSearchCondition(cond))
                .fetch();

        return groupingRevisionEntityToCommitDto(revisions);
    }

    private List<CommitRsDto> groupingRevisionEntityToCommitDto(List<Revision> revisions){
        List<CommitRsDto> retDto = new ArrayList<>();

        Map<Commit, List<Revision>> reviMap = revisions.stream()
                .collect(Collectors.groupingBy(Revision::getCommit));

        reviMap.forEach((c, revisionList) -> {
            List<RevisionRsDto> revisionRsDtos = revisionList.stream()
                    .map(r -> new RevisionRsDto(r.getType(), r.getVersion(), r.getFile().getName(), r.getFile().getPath()))
                    .toList();

            CommitRsDto commitRsDto = new CommitRsDto(
                    c.getCommitMsg(),
                    c.getProject().getName(),
                    c.getUser().getName(),
                    c.getCommitTime(),
                    (long) revisionList.size(),
                    revisionRsDtos);

            retDto.add(commitRsDto);
        });

        return retDto;
    }

    /******************************************************************************************
     * 검색 조건
     ******************************************************************************************/
    private BooleanBuilder commitSearchCondition(CommitRqCond cond){
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(projectNameEq(cond.getProject()))
                .and(userNameEq(cond.getUser()));

        return builder;
    }

    private BooleanExpression projectNameEq(String projectName){
        return hasLength(projectName) ? project.name.equalsIgnoreCase(projectName) : null;
    }

    private BooleanExpression userNameEq(String userName){
        return hasLength(userName) ? user.name.equalsIgnoreCase(userName) : null;
    }
}
