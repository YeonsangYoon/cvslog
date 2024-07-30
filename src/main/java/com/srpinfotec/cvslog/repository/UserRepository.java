package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

    Optional<User> findByName(String name);
}
