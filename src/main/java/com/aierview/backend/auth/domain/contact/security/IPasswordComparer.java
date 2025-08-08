package com.aierview.backend.auth.domain.contact.security;

public interface IPasswordComparer {
    boolean matches(String password, String hashedPassword);
}
