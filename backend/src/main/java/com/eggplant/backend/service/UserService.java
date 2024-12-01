package com.eggplant.backend.service;

import com.eggplant.backend.entity.User;
import com.eggplant.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /**
     * 새로운 유저를 생성하고 저장하는 메소드
     * @param username 유저 이름
     * @param email 이메일
     * @param password 암호화되지 않은 비밀번호
     * @return 저장된 User 객체
     */
    @Transactional
    public User createUser(String username, String email, String password) {
        // User 객체 생성
        User newUser = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .lastLogin(LocalDateTime.now())
                .build();

        // 데이터베이스에 저장
        return userRepository.save(newUser);
    }


    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with ID " + id + " does not exist.");
        }
        userRepository.deleteById(id);
    }


    public List<User> getUsers(){
        return userRepository.findAll();
    }
}