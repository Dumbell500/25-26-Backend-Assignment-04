package com.gdg.jwtexample.controller;

import com.gdg.jwtexample.domain.User;
import com.gdg.jwtexample.dto.user.UserMeResponse;
import com.gdg.jwtexample.repository.UserRepository;
import java.security.Principal;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> me(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        return ResponseEntity.ok(UserMeResponse.from(user));
    }
}
