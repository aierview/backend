package com.aierview.backend.auth.domain.contact.security;

public interface IPasswordEncoder {
    String encode(String password);
}
