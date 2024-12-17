package com.srpinfotec.api.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srpinfotec.api.dto.response.UserRsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.srpinfotec.core.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
    private final JPAQueryFactory queryFactory;

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
