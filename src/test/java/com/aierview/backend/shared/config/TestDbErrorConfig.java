package com.aierview.backend.shared.config;

import com.aierview.backend.auth.usecase.contract.google.IGoogleSignup;
import com.aierview.backend.auth.usecase.contract.lcoal.ILocalSignin;
import com.aierview.backend.auth.usecase.contract.lcoal.ILocalSignup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test-db-error")
public class TestDbErrorConfig {
    @Bean
    public ILocalSignup testDatabaseErrorLocalSignup() {
        return request -> {
            throw new RuntimeException("Simulated DB error");
        };
    }

    @Bean
    public ILocalSignin testDatabaseErrorLocalSigin() {
        return request -> {
            throw new RuntimeException("Simulated DB error");
        };
    }

    @Bean
    public IGoogleSignup testDatabaseErrorGoogleSignup() {
        return request -> {
            throw new RuntimeException("Simulated DB error");
        };
    }
}
