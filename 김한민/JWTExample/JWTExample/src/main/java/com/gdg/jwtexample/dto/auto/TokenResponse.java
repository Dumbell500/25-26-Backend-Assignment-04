package com.gdg.jwtexample.dto.auto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
