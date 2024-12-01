package com.eggplant.backend.controller;

import com.eggplant.backend.dto.SignUpUser;
import com.eggplant.backend.entity.User;
import com.eggplant.backend.jwt.JWTUtil;
import com.eggplant.backend.service.CustomUserDetailsService;
import com.eggplant.backend.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public String login(@RequestParam String username, @RequestParam String password) throws AuthenticationException {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @PostMapping("/token/validation")
    @ResponseStatus(HttpStatus.OK)
    public void jwtValidate(@RequestParam String token) {
        if(!jwtUtil.validateToken(token)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token is invalid.");
        }
    }
}
