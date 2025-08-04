package com.aierview.backend.auth.domain.security;

public interface IPasswordEncoder {
    String encode(String password);
}
