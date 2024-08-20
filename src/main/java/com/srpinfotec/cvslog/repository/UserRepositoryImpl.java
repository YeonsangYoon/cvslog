package com.srpinfotec.cvslog.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srpinfotec.cvslog.domain.QUser;
import com.srpinfotec.cvslog.domain.User;
import com.srpinfotec.cvslog.dto.response.UserRsDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.srpinfotec.cvslog.domain.QUser.user;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findByNaturalId(Object name) {
        return entityManager.unwrap(Session.class)
                .byNaturalId(User.class)
                .using("name", name)
                .loadOptional();
    }

    @Override
    public List<UserRsDto> findAllDtos() {
        return queryFactory
                .select(Projections.constructor(UserRsDto.class,
                        user.id,
                        user.name))
                .from(user)
                .orderBy(user.name.asc())
                .fetch();
    }
}
