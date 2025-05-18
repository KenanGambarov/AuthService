package com.authservice.repository;

import com.authservice.entity.TokenEntity;
import com.authservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByToken(String token);

    @Query("SELECT t FROM TokenEntity t WHERE t.user.id = :userId AND t.expired = false AND t.revoked = false")
    Optional<List<TokenEntity>> findAllValidTokenByUser(@Param("userId") Long userId);

//    void revokeAllUserTokens(Long id);
}
