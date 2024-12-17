package com.srpinfotec.core.repository;

import com.srpinfotec.core.entity.Project;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Optional<Project> findByNaturalId(Object name) {
        return entityManager.unwrap(Session.class)
                .byNaturalId(Project.class)
                .using("name", name)
                .loadOptional();
    }
}
