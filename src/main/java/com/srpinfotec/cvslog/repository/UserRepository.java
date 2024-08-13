package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByName(String name);

    @Query("select u from User u order by u.name")
    List<User> findOrderByName();
}
