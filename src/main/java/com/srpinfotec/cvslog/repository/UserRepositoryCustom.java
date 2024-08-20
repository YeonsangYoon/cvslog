package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.User;
import com.srpinfotec.cvslog.dto.response.UserRsDto;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findByNaturalId(Object name);

    List<UserRsDto> findAllDtos();
}
