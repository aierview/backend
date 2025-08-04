package com.aierview.backend.auth.infra.config;

import com.aierview.backend.auth.domain.repository.IAuthRepository;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.auth.domain.security.IPasswordEncoder;
import com.aierview.backend.auth.usecase.impl.LocalSignup;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test-db-error")
@RequiredArgsConstructor
public class AuthUseCaseConfig {
    private final IUserRepository userRepository;
    private final IPasswordEncoder passwordEncoder;
    private final IAuthRepository authRepository;

    @Bean
    public LocalSignup localSignup() {
        return new LocalSignup(userRepository, passwordEncoder, authRepository);
    }
}
