package com.aierview.backend.auth.infra.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BCryptPasswordEncoderAdapterTests {
    private BCryptPasswordEncoderAdapter adapter;
    private PasswordEncoder encoder;


    @BeforeEach
    void setUp() {
        this.encoder = mock(PasswordEncoder.class);
        this.adapter = new BCryptPasswordEncoderAdapter(encoder);
    }

    @Test
    @DisplayName("Should encode password")
    void shouldEncodePassword() {
        String password = "anyPassword";
        String encodedPassword = UUID.randomUUID().toString();
        when(encoder.encode(password)).thenReturn(encodedPassword);

        String encoded = adapter.encode(password);

        assertEquals(encodedPassword, encoded);
        verify(encoder, Mockito.times(1)).encode(password);
    }

    @Test
    @DisplayName("Should return false when password does not match")
    void shouldReturnFalseWhenPasswordDoesNotMatch() {
        String password = "anyPassword";
        String encodedPassword = UUID.randomUUID().toString();

        when(this.encoder.matches(password, encodedPassword)).thenReturn(false);

        boolean result = this.adapter.matches(password, encodedPassword);

        assertFalse(result);
        verify(encoder, Mockito.times(1)).matches(password,encodedPassword);
    }


    @Test
    @DisplayName("Should return true when password matches")
    void shouldReturnTrueWhenPasswordMatches() {
        String password = "anyPassword";
        String encodedPassword = UUID.randomUUID().toString();

        when(this.encoder.matches(password, encodedPassword)).thenReturn(true);

        boolean result = this.adapter.matches(password, encodedPassword);

        assertTrue(result);
        verify(encoder, Mockito.times(1)).matches(password,encodedPassword);
    }
}
