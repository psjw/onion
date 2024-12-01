package com.eggplant.backend.controller;

import com.eggplant.backend.dto.SignUpUser;
import com.eggplant.backend.entity.User;
import com.eggplant.backend.jwt.JWTUtil;
import com.eggplant.backend.service.CustomUserDetailsService;
import com.eggplant.backend.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> createUser(SignUpUser signUpUser) {
        User createdUser = userService.createUser(signUpUser);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to be deleted", required = true) @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String token = jwtUtil.generateToken(userDetails.getUsername());
        // 쿠키 생성
        ResponseCookie jwtCookie = ResponseCookie.from("onion_token", token)
                .httpOnly(true) // 자바스크립트에서 접근 불가능
//                .secure(true) // HTTPS에서만 전송
                .path("/") // 모든 경로에서 유효
                .maxAge(60 * 60) // 1시간 동안 유효
                .sameSite("Strict") // CSRF 공격 방지
                .build();
        response.addHeader("Set-Cookie", jwtCookie.toString());
        // 쿠키를 응답에 추가
        return token;
    }

    @PostMapping("/token/validation")
    @ResponseStatus(HttpStatus.OK)
    public void jwtValidate(@RequestParam String token) {
        if(!jwtUtil.validateToken(token)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token is invalid.");
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("onion_token", null);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); //HTTPS에서만 전송
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
