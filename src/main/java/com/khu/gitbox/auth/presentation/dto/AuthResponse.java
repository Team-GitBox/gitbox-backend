package com.khu.gitbox.auth.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record AuthResponse(String accessToken,
                           @JsonIgnore
                           String refreshToken) {

    public static AuthResponse from(String accessToken, String refreshToken) {
        return new AuthResponse(accessToken, refreshToken);
    }
}

