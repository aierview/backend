package com.aierview.backend.shared.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FuncUtilsTests {

    @Test
    @DisplayName("Should return capitalized string")
    void capitalize() {
        FuncUtils funcUtils = new FuncUtils();
        String name = " john snow smith ";
        String capitalized = FuncUtils.formatToCapitalize(name);
        Assertions.assertEquals("John Snow Smith", capitalized);
    }

    @Test
    @DisplayName("Should return lowercase string")
    void lowerCase() {
        String name = " john snow smith ";
        String lowercase = FuncUtils.formatLowercase(name);
        Assertions.assertEquals("john snow smith", lowercase);
    }
}
