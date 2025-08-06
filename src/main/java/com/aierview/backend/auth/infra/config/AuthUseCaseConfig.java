package com.aierview.backend.auth.infra.config;

import com.aierview.backend.auth.domain.enums.Environment;
import com.aierview.backend.auth.domain.repository.IAuthRepository;
import com.aierview.backend.auth.domain.repository.IUserRepository;
import com.aierview.backend.auth.domain.security.IPasswordComparer;
import com.aierview.backend.auth.domain.security.IPasswordEncoder;
import com.aierview.backend.auth.domain.token.ITokenGenerator;
import com.aierview.backend.auth.usecase.contract.IGenerateCookieResponse;
import com.aierview.backend.auth.usecase.impl.GenerateCookieResponse;
import com.aierview.backend.auth.usecase.impl.LocalSignin;
import com.aierview.backend.auth.usecase.impl.LocalSignup;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final IPasswordComparer passwordComparer;
    private final ITokenGenerator tokenGenerator;

    @Value("${spring.profiles.active}")
    private String env;

    @Bean
    public LocalSignup localSignup() {
        return new LocalSignup(userRepository, passwordEncoder, authRepository);
    }

    @Bean
    public LocalSignin localSignin() {
        return  new LocalSignin(userRepository,authRepository,passwordComparer,tokenGenerator,generateCookieResponse());
    }

    @Bean
    public IGenerateCookieResponse generateCookieResponse() {
        return new GenerateCookieResponse(Environment.valueOf(env.toUpperCase()));
    }
}
