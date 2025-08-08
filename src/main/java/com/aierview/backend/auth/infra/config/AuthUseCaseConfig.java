package com.aierview.backend.auth.infra.config;

import com.aierview.backend.auth.domain.contact.repository.IAuthRepository;
import com.aierview.backend.auth.domain.contact.repository.IUserRepository;
import com.aierview.backend.auth.domain.contact.security.IPasswordComparer;
import com.aierview.backend.auth.domain.contact.security.IPasswordEncoder;
import com.aierview.backend.auth.domain.contact.token.ITokenGenerator;
import com.aierview.backend.auth.domain.enums.Environment;
import com.aierview.backend.auth.usecase.contract.cookie.IGenerateCookieResponse;
import com.aierview.backend.auth.usecase.contract.lcoal.ILocalSignin;
import com.aierview.backend.auth.usecase.contract.lcoal.ILocalSignup;
import com.aierview.backend.auth.usecase.impl.cookie.GenerateCookieResponse;
import com.aierview.backend.auth.usecase.impl.local.LocalSignin;
import com.aierview.backend.auth.usecase.impl.local.LocalSignup;
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
    public ILocalSignup localSignup() {
        return new LocalSignup(userRepository, passwordEncoder, authRepository);
    }

    @Bean
    public ILocalSignin localSignin() {
        return new LocalSignin(userRepository, authRepository, passwordComparer, tokenGenerator, generateCookieResponse());
    }

    @Bean
    public IGenerateCookieResponse generateCookieResponse() {
        return new GenerateCookieResponse(Environment.valueOf(env.toUpperCase()));
    }
}
