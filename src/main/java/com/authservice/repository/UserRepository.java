package com.authservice.repository;

import com.authservice.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {


    boolean existsByUsername(String username);

    @EntityGraph(attributePaths = {"userRoles","userRoles.role"})
    Optional<UserEntity> findByUsername(String username);

 }
