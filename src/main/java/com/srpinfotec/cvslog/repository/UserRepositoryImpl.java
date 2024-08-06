package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Optional<User> findByNaturalId(Object name) {
        return entityManager.unwrap(Session.class)
                .byNaturalId(User.class)
                .using("name", name)
                .loadOptional();
    }
}
