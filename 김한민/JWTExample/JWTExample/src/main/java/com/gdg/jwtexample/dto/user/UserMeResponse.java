package com.gdg.jwtexample.dto.user;

import com.gdg.jwtexample.domain.User;

public record UserMeResponse(Long id, String email, String role) {
    public static UserMeResponse from(User u) {
        return new UserMeResponse(u.getId(), u.getEmail(), u.getRole().toString());
    }
}
