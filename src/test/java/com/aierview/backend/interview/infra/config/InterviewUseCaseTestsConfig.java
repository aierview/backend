package com.aierview.backend.interview.infra.config;

import com.aierview.backend.interview.usecase.contract.IBeginInterview;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test-db-error")
public class InterviewUseCaseTestsConfig {
    @Bean
    public IBeginInterview testDatabaseErrorBeginInterview() {
        return request -> {
            throw new RuntimeException("Simulated DB error");
        };
    }
}
