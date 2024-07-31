package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
}
