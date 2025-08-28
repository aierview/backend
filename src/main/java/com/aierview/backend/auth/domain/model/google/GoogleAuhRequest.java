package com.aierview.backend.auth.domain.model.google;

import jakarta.validation.constraints.NotBlank;

public record GoogleAuhRequest(@NotBlank(message = "Id token is required!") String idToken) {
}
