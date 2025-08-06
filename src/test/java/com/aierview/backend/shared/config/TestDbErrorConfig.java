package com.aierview.backend.shared.config;

import com.aierview.backend.auth.usecase.contract.ILocalSignin;
import com.aierview.backend.auth.usecase.contract.ILocalSignup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test-db-error")
public class TestDbErrorConfig {
    @Bean
    public ILocalSignup testDatabaseErrorSignup() {
        return request -> {
            throw new RuntimeException("Simulated DB error");
        };
    }

    @Bean
    public ILocalSignin testDatabaseErrorSigin() {
        return request -> {
            throw new RuntimeException("Simulated DB error");
        };
    }
}
