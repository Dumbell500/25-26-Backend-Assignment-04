package com.gdg.jwtexample.service;

import com.gdg.jwtexample.domain.RefreshToken;
import com.gdg.jwtexample.domain.User;
import com.gdg.jwtexample.domain.UserRole;
import com.gdg.jwtexample.dto.auto.LoginRequest;
import com.gdg.jwtexample.dto.auto.SignupRequest;
import com.gdg.jwtexample.dto.auto.TokenResponse;
import com.gdg.jwtexample.jwt.JwtTokenProvider;
import com.gdg.jwtexample.repository.RefreshTokenRepository;
import com.gdg.jwtexample.repository.UserRepository;
import java.time.Instant;
import java.util.NoSuchElementException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;
    private final BCryptPasswordEncoder encoder;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       JwtTokenProvider tokenProvider,
                       BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.encoder = encoder;
    }

    public void signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(req.email())
                .password(encoder.encode(req.password()))
                .role(UserRole.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    public TokenResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String access = tokenProvider.createAccessToken(user.getEmail(), user.getRole());
        String refresh = tokenProvider.createRefreshToken(user.getEmail());

        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);

        refreshTokenRepository.save(new RefreshToken(
                user,
                refresh,
                Instant.now().plusMillis(7L * 24 * 60 * 60 * 1000)
        ));

        return new TokenResponse(access, refresh);
    }

    public TokenResponse reissue(String refreshToken) {
        if (!tokenProvider.isValid(refreshToken)) {
            throw new IllegalArgumentException("리프레시 토큰이 유효하지 않습니다.");
        }

        String email = tokenProvider.parseClaims(refreshToken).getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        RefreshToken saved = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("저장된 리프레시 토큰이 없습니다."));

        if (!saved.getToken().equals(refreshToken)) {
            throw new IllegalArgumentException("리프레시 토큰이 일치하지 않습니다.");
        }

        String newAccess = tokenProvider.createAccessToken(user.getEmail(), user.getRole());
        String newRefresh = tokenProvider.createRefreshToken(user.getEmail());

        saved.rotate(newRefresh, Instant.now().plusMillis(7L * 24 * 60 * 60 * 1000));

        return new TokenResponse(newAccess, newRefresh);
    }
}
