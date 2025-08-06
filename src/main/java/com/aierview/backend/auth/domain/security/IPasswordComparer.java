package com.aierview.backend.auth.domain.security;

public interface IPasswordComparer {
    boolean matches(String password, String hashedPassword);
}
