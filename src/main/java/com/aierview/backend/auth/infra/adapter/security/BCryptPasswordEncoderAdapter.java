package com.aierview.backend.auth.infra.adapter.security;

import com.aierview.backend.auth.domain.contact.security.IPasswordComparer;
import com.aierview.backend.auth.domain.contact.security.IPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BCryptPasswordEncoderAdapter implements IPasswordEncoder, IPasswordComparer {
    private final PasswordEncoder encoder;

    @Override
    public String encode(String password) {
        return this.encoder.encode(password);
    }

    @Override
    public boolean matches(String password, String hashedPassword) {
        return this.encoder.matches(password, hashedPassword);
    }
}
