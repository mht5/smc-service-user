package com.smc.repository;

import com.smc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") int id);

}