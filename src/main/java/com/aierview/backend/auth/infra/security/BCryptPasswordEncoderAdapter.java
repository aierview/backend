package com.aierview.backend.auth.infra.security;

import com.aierview.backend.auth.domain.security.IPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BCryptPasswordEncoderAdapter implements IPasswordEncoder {
    private final PasswordEncoder encoder;

    @Override
    public String encode(String password) {
        return this.encoder.encode(password);
    }
}
