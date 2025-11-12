package com.gdg.jwtexample.controller;

import com.gdg.jwtexample.dto.auto.LoginRequest;
import com.gdg.jwtexample.dto.auto.ReissueRequest;
import com.gdg.jwtexample.dto.auto.SignupRequest;
import com.gdg.jwtexample.dto.auto.TokenResponse;
import com.gdg.jwtexample.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequest req) {
        authService.signup(req);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody @Valid ReissueRequest req) {
        return ResponseEntity.ok(authService.reissue(req.refreshToken()));
    }
}
