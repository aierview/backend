package com.aierview.backend.auth.infra.api.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleSignupRequest(@NotBlank(message = "Id token is required!")  String idToken) {
}
