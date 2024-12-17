package com.srpinfotec.core.repository;

import com.srpinfotec.core.entity.User;
import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findByNaturalId(Object name);
}
