package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.User;

import java.util.Optional;

public interface UserCustomRepository {
    Optional<User> findByNaturalId(Object name);
}
