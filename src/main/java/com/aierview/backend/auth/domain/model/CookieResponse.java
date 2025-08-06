package com.aierview.backend.auth.domain.model;

public record CookieResponse(
        String name,
        String value,
        boolean httpOnly,
        boolean secure,
        String sameSite,
        String path
) {
}
