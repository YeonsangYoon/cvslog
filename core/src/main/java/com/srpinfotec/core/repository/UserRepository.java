package com.srpinfotec.core.repository;

import com.srpinfotec.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByName(String name);

    @Query("select u from User u order by u.name")
    List<User> findOrderByName();
}
