package com.aierview.backend.auth.domain.model.cookie;

public record CookieResponse(
        String name,
        String value,
        boolean httpOnly,
        boolean secure,
        String sameSite,
        String path
) {
}
