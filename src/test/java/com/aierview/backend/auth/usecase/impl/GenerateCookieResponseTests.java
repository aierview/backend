package com.aierview.backend.auth.usecase.impl;

import com.aierview.backend.auth.domain.model.CookieResponse;
import com.aierview.backend.auth.usecase.contract.IGenerateCookieResponse;
import com.aierview.backend.shared.enums.Environment;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class GenerateCookieResponseTests {
    private IGenerateCookieResponse generateCookieResponse;


    @Test
    @DisplayName("Should return cookie response for prod env")
    void shouldReturnCookieResponseForProdEnv() {
        String token = UUID.randomUUID().toString();
        CookieResponse cookieResponse = AuthTestFixture.anyProdCookieResponse(token);

        this.generateCookieResponse = new GenerateCookieResponse(Environment.PROD);
        CookieResponse response = this.generateCookieResponse.generate("token", token);

        Assertions.assertNotNull(this.generateCookieResponse);
        Assertions.assertEquals(response.name(), cookieResponse.name());
        Assertions.assertEquals(response.value(), cookieResponse.value());
        Assertions.assertEquals(response.httpOnly(), cookieResponse.httpOnly());
        Assertions.assertEquals(response.secure(), cookieResponse.secure());
        Assertions.assertEquals(response.sameSite(), cookieResponse.sameSite());
        Assertions.assertEquals(response.path(), cookieResponse.path());
    }

    @Test
    @DisplayName("Should return cookie response for homolog env")
    void shouldReturnCookieResponseForHomologEnv() {
        String token = UUID.randomUUID().toString();
        CookieResponse cookieResponse = AuthTestFixture.anyHomologCookieResponse(token);

        this.generateCookieResponse = new GenerateCookieResponse(Environment.HOMOLOG);
        CookieResponse response = this.generateCookieResponse.generate("token", token);

        Assertions.assertNotNull(this.generateCookieResponse);
        Assertions.assertEquals(response.name(), cookieResponse.name());
        Assertions.assertEquals(response.value(), cookieResponse.value());
        Assertions.assertEquals(response.httpOnly(), cookieResponse.httpOnly());
        Assertions.assertEquals(response.secure(), cookieResponse.secure());
        Assertions.assertEquals(response.sameSite(), cookieResponse.sameSite());
        Assertions.assertEquals(response.path(), cookieResponse.path());
    }

    @Test
    @DisplayName("Should return cookie response for dev env")
    void shouldReturnCookieResponseForDevEnv() {
        String token = UUID.randomUUID().toString();
        CookieResponse cookieResponse = AuthTestFixture.anyDevCookieResponse(token);

        this.generateCookieResponse = new GenerateCookieResponse(Environment.DEV);
        CookieResponse response = this.generateCookieResponse.generate("token", token);

        Assertions.assertNotNull(this.generateCookieResponse);
        Assertions.assertEquals(response.name(), cookieResponse.name());
        Assertions.assertEquals(response.value(), cookieResponse.value());
        Assertions.assertEquals(response.httpOnly(), cookieResponse.httpOnly());
        Assertions.assertEquals(response.secure(), cookieResponse.secure());
        Assertions.assertEquals(response.sameSite(), cookieResponse.sameSite());
        Assertions.assertEquals(response.path(), cookieResponse.path());
    }
}
