package com.eggplant.backend.repository;

import com.eggplant.backend.entity.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {
    Optional<JwtBlacklist> findByToken(String token);

    @Query(value = "SELECT * FROM jwt_blacklist WHERE username = :username ORDER BY expiration_time LIMIT 1", nativeQuery = true)
    Optional<JwtBlacklist> findTopByUsernameOrderByExpirationTime(@Param("username") String username);
}