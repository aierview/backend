package com.aierview.backend.auth.domain.model.google;

import jakarta.validation.constraints.NotBlank;

public record GoogleSignupRequest(@NotBlank(message = "Id token is required!") String idToken) {
}
