package com.aierview.backend.shared.utils;

import com.aierview.backend.auth.domain.model.CookieResponse;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FuncUtils {
    public static String formatToCapitalize(String text) {
        return Arrays.stream(text.trim().toLowerCase().split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    public static String formatLowercase(String text) {
        return text.trim().toLowerCase();
    }

    public static String cookieFromCookieResponse(CookieResponse cookieResponse) {
        return ResponseCookie.from(cookieResponse.name(), cookieResponse.value())
                .httpOnly(cookieResponse.httpOnly())
                .secure(cookieResponse.secure())
                .sameSite(cookieResponse.sameSite())
                .path(cookieResponse.path())
                .build().toString();
    }
}

