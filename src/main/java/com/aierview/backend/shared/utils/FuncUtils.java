package com.aierview.backend.shared.utils;

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
}

