package com.eggplant.backend.repository;

import com.eggplant.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일을 기반으로 사용자 찾기
    Optional<User> findByEmail(String email);

    // username을 기반으로 사용자 찾기
    Optional<User> findByUsername(String username);
}